package org.example.util;

import org.example.dto.DistilleryDto;
import org.example.dto.StoreDto;
import org.example.dto.VineDto;
import org.example.entity.Distillery;
import org.example.entity.Store;
import org.example.entity.Vine;

public class TestObjectInitializer {

    public static DistilleryDto initializeDistilleryDto() {
        return DistilleryDto.builder()
                .id(1L)
                .name("TestDistillery")
                .build();
    }

    public static Distillery initializeDistillery() {
        return Distillery.builder()
                .id(1L)
                .name("TestDistillery")
                .build();
    }

    public static StoreDto initializeStoreDto() {
        return StoreDto.builder()
                .id(1L)
                .name("TestStore")
                .build();
    }

    public static Store initializeStore() {
        return Store.builder()
                .id(1L)
                .name("TestStore")
                .build();
    }

    public static VineDto initializeVineDto() {
        return VineDto.builder()
                .id(1L)
                .name("TestVine")
                .storeID(1L)
                .build();
    }

    public static Vine initializeVine() {
        return Vine.builder()
                .id(1L)
                .name("TestVine")
                .storeID(1L)
                .build();
    }
}