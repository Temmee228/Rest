package org.example.mapper.impl;

import org.example.dto.VineDto;
import org.example.entity.Vine;
import org.example.mapper.Mapper;

public class VineMapperImpl implements Mapper<Vine, VineDto> {
    @Override
    public VineDto toDto(Vine vine) {
        return VineDto.builder().id(vine.getId())
                .name(vine.getName())
                .storeID(vine.getStoreID())
                .build();
    }

    @Override
    public Vine toEntity(VineDto vineDto) {
        return Vine.builder().id(vineDto.getId())
                .name(vineDto.getName())
                .storeID(vineDto.getStoreID())
                .build();
    }
}
