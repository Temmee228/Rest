package org.example.mapper;

import org.example.dto.StoreDto;
import org.example.entity.Store;
import org.example.mapper.impl.StoreMapperImpl;
import org.example.util.TestObjectInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class StoreMapperTest {
    @Mock
    private StoreMapperImpl storeMapper;

    private StoreDto expectedStoreDto;
    private Store expectedStore;

    @BeforeEach
    void setUp() {
        expectedStoreDto = TestObjectInitializer.initializeStoreDto();
        expectedStore = TestObjectInitializer.initializeStore();
    }

    @Test
    void toDto() {
        when(storeMapper.toDto(expectedStore)).thenReturn(expectedStoreDto);
        StoreDto storeDto = storeMapper.toDto(expectedStore);
        assertEquals(expectedStoreDto, storeDto);
    }

    @Test
    void toEntityTest() {
        when(storeMapper.toEntity(expectedStoreDto)).thenReturn(expectedStore);
        Store store = storeMapper.toEntity(expectedStoreDto);
        assertEquals(expectedStore, store);
    }
}
