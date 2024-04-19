package org.example.mapper.impl;


import org.example.dto.DistilleryDto;
import org.example.entity.Distillery;
import org.example.mapper.Mapper;

public class DistilleryMapperImpl implements Mapper<Distillery, DistilleryDto> {
    @Override
    public DistilleryDto toDto(Distillery distillery) {
        return DistilleryDto.builder().id(distillery.getId())
                .name(distillery.getName())
                .build();
    }

    @Override
    public Distillery toEntity(DistilleryDto distilleryDto) {
        return Distillery.builder().id(distilleryDto.getId())
                .name(distilleryDto.getName())
                .build();
    }
}
