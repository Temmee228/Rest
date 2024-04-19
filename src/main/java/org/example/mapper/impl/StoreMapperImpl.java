package org.example.mapper.impl;

import org.example.dto.StoreDto;
import org.example.entity.Store;
import org.example.mapper.Mapper;

public class StoreMapperImpl implements Mapper<Store, StoreDto> {
    @Override
    public StoreDto toDto(Store store) {
        return StoreDto.builder().id(store.getId())
                .name(store.getName())
                .build();
    }

    @Override
    public Store toEntity(StoreDto storeDto) {
        return Store.builder().id(storeDto.getId())
                .name(storeDto.getName())
                .build();
    }
}
