package org.example.service;

import org.example.dto.DistilleryDto;
import org.example.entity.Distillery;
import org.example.mapper.impl.DistilleryMapperImpl;
import org.example.repository.impl.DistilleryRepository;
import org.example.service.impl.DistilleryService;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistilleryServiceTest {
    @Mock
    private DistilleryRepository distilleryRepository;
    @Mock
    private DistilleryMapperImpl distilleryMapper;
    @InjectMocks
    private DistilleryService distilleryService;
    @Spy
    private DistilleryService spyService;

    private DistilleryDto expectedDistilleryDto;
    private Distillery expectedDistillery;

    @BeforeEach
    void setUp() {
        distilleryService = new DistilleryService(distilleryRepository, distilleryMapper);
        spyService = spy(distilleryService);

        expectedDistilleryDto = TestObjectInitializer.initializeDistilleryDto();
        expectedDistillery = TestObjectInitializer.initializeDistillery();
    }

    @Test
    void getAllTest() {
        when(distilleryRepository.findAll()).thenReturn(List.of(expectedDistillery));
        when(distilleryMapper.toDto(expectedDistillery)).thenReturn(expectedDistilleryDto);

        List<DistilleryDto> distilleryDtoList = spyService.getAll();

        verify(distilleryMapper).toDto(expectedDistillery);
        assertEquals(List.of(expectedDistilleryDto), distilleryDtoList);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(distilleryRepository.findOne(id)).thenReturn(expectedDistillery);
        when(spyService.getById(id)).thenReturn(expectedDistilleryDto);

        DistilleryDto distilleryDto = spyService.getById(id);

        verify(distilleryMapper).toDto(expectedDistillery);
        assertEquals(expectedDistilleryDto, distilleryDto);
    }

    @Test
    void saveTest() {
        List<Long> vineIds = List.of(1L);
        when(distilleryMapper.toEntity(expectedDistilleryDto)).thenReturn(expectedDistillery);
        when(distilleryRepository.save(expectedDistillery, vineIds)).thenReturn(expectedDistillery);
        when(distilleryMapper.toDto(expectedDistillery)).thenReturn(expectedDistilleryDto);

        DistilleryDto savedDistilleryDto = spyService.save(expectedDistilleryDto, vineIds);

        verify(distilleryRepository).save(expectedDistillery, vineIds);
        verify(distilleryMapper).toDto(expectedDistillery);
        verify(distilleryMapper).toEntity(expectedDistilleryDto);
        assertEquals(expectedDistilleryDto, savedDistilleryDto);
    }

    @Test
    void updateTest() {
        when(distilleryMapper.toDto(expectedDistillery)).thenReturn(expectedDistilleryDto);
        when(distilleryMapper.toEntity(expectedDistilleryDto)).thenReturn(expectedDistillery);
        when(distilleryRepository.update(eq(1L), eq(expectedDistillery))).thenReturn(expectedDistillery);

        DistilleryDto result = spyService.update(1L, expectedDistilleryDto);

        verify(distilleryMapper).toDto(expectedDistillery);
        verify(distilleryMapper).toEntity(expectedDistilleryDto);
        verify(distilleryRepository).update(eq(1L), eq(expectedDistillery));
        assertEquals(expectedDistilleryDto, result);
    }

    @Test
    void removeTest() {
        when(distilleryRepository.remove(1L)).thenReturn(true);

        boolean result = spyService.remove(1L);

        assertTrue(result);
    }
}
