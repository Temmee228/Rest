package org.example.service;

import org.example.dto.VineDto;
import org.example.entity.Vine;
import org.example.mapper.impl.VineMapperImpl;
import org.example.repository.impl.VineRepository;
import org.example.service.impl.VineService;
import org.example.util.TestObjectInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VineServiceTest {
    @Mock
    private VineRepository vineRepository;
    @Mock
    private VineMapperImpl vineMapper;
    @InjectMocks
    private VineService vineService;
    @Spy
    private VineService spyService;

    private VineDto expectedVineDto;
    private Vine expectedVine;

    @BeforeEach
    void setUp() {
        vineService = new VineService(vineRepository, vineMapper);
        spyService = spy(vineService);

        expectedVineDto = TestObjectInitializer.initializeVineDto();
        expectedVine = TestObjectInitializer.initializeVine();
    }

    @Test
    void getAllTest() {
        when(vineRepository.findAll()).thenReturn(List.of(expectedVine));
        when(vineMapper.toDto(expectedVine)).thenReturn(expectedVineDto);

        List<VineDto> vineDtoList = spyService.getAll();

        verify(vineMapper).toDto(expectedVine);
        assertEquals(List.of(expectedVineDto), vineDtoList);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(vineRepository.findOne(id)).thenReturn(expectedVine);
        when(spyService.getById(id)).thenReturn(expectedVineDto);

        VineDto vineDto = spyService.getById(id);

        verify(vineMapper).toDto(expectedVine);
        assertEquals(expectedVineDto, vineDto);
    }

    @Test
    void saveTest() {
        when(vineMapper.toEntity(expectedVineDto)).thenReturn(expectedVine);
        when(vineRepository.save(expectedVine)).thenReturn(expectedVine);
        when(vineMapper.toDto(expectedVine)).thenReturn(expectedVineDto);

        VineDto savedVineDto = spyService.save(expectedVineDto);

        verify(vineRepository).save(expectedVine);
        verify(vineMapper).toDto(expectedVine);
        verify(vineMapper).toEntity(expectedVineDto);
        assertEquals(expectedVineDto, savedVineDto);
    }

    @Test
    void updateTest() {
        when(vineMapper.toDto(expectedVine)).thenReturn(expectedVineDto);
        when(vineMapper.toEntity(expectedVineDto)).thenReturn(expectedVine);
        when(vineRepository.update(eq(1L), eq(expectedVine))).thenReturn(expectedVine);

        VineDto result = spyService.update(1L, expectedVineDto);

        verify(vineMapper).toDto(expectedVine);
        verify(vineMapper).toEntity(expectedVineDto);
        verify(vineRepository).update(eq(1L), eq(expectedVine));
        assertEquals(expectedVineDto, result);
    }

    @Test
    void removeTest() {
        when(vineRepository.remove(1L)).thenReturn(true);

        boolean result = spyService.remove(1L);

        assertTrue(result);
    }
}
