package org.example.service;

import org.example.dto.StoreDto;
import org.example.entity.Store;
import org.example.mapper.impl.StoreMapperImpl;
import org.example.repository.impl.StoreRepository;
import org.example.service.impl.StoreService;
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
public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreMapperImpl storeMapper;
    @InjectMocks
    private StoreService storeService;
    @Spy
    private StoreService spyService;

    private StoreDto expectedStoreDto;
    private Store expectedStore;

    @BeforeEach
    void setUp() {
        storeService = new StoreService(storeRepository, storeMapper);
        spyService = spy(storeService);

        expectedStoreDto = TestObjectInitializer.initializeStoreDto();
        expectedStore = TestObjectInitializer.initializeStore();
    }

    @Test
    void getAllTest() {
        when(storeRepository.findAll()).thenReturn(List.of(expectedStore));
        when(storeMapper.toDto(expectedStore)).thenReturn(expectedStoreDto);

        List<StoreDto> storeDtoList = spyService.getAll();

        verify(storeMapper).toDto(expectedStore);
        assertEquals(List.of(expectedStoreDto), storeDtoList);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(storeRepository.findOne(id)).thenReturn(expectedStore);
        when(spyService.getById(id)).thenReturn(expectedStoreDto);

        StoreDto storeDto = spyService.getById(id);

        verify(storeMapper).toDto(expectedStore);
        assertEquals(expectedStoreDto, storeDto);
    }

    @Test
    void saveTest() {
        when(storeMapper.toEntity(expectedStoreDto)).thenReturn(expectedStore);
        when(storeRepository.save(expectedStore)).thenReturn(expectedStore);
        when(storeMapper.toDto(expectedStore)).thenReturn(expectedStoreDto);

        StoreDto savedStoreDto = spyService.save(expectedStoreDto);

        verify(storeRepository).save(expectedStore);
        verify(storeMapper).toDto(expectedStore);
        verify(storeMapper).toEntity(expectedStoreDto);
        assertEquals(expectedStoreDto, savedStoreDto);
    }

    @Test
    void updateTest() {
        when(storeMapper.toDto(expectedStore)).thenReturn(expectedStoreDto);
        when(storeMapper.toEntity(expectedStoreDto)).thenReturn(expectedStore);
        when(storeRepository.update(eq(1L), eq(expectedStore))).thenReturn(expectedStore);

        StoreDto result = spyService.update(1L, expectedStoreDto);

        verify(storeMapper).toDto(expectedStore);
        verify(storeMapper).toEntity(expectedStoreDto);
        verify(storeRepository).update(eq(1L), eq(expectedStore));
        assertEquals(expectedStoreDto, result);
    }

    @Test
    void removeTest() {
        when(storeRepository.remove(1L)).thenReturn(true);

        boolean result = spyService.remove(1L);

        assertTrue(result);
    }
}
