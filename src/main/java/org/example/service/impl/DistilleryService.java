package org.example.service.impl;

import org.example.dto.DistilleryDto;
import org.example.entity.Distillery;
import org.example.mapper.Mapper;
import org.example.mapper.impl.DistilleryMapperImpl;
import org.example.repository.impl.DistilleryRepository;
import org.example.service.Service;

import java.util.List;

public class DistilleryService implements Service<DistilleryDto> {
    private final DistilleryRepository distilleryRepository;
    private final Mapper<Distillery, DistilleryDto> distilleryMapper;
    public DistilleryService(){
        distilleryMapper = new DistilleryMapperImpl();
        distilleryRepository = new DistilleryRepository();
    }

    public DistilleryService(DistilleryRepository rep, DistilleryMapperImpl map){
        distilleryRepository = rep;
        distilleryMapper = map;
    }

    @Override
    public List<DistilleryDto> getAll() {
        List<Distillery> distilleries = distilleryRepository.findAll();
        return distilleries.stream().map(distilleryMapper::toDto).toList();
    }

    @Override
    public DistilleryDto getById(long id) {
        return distilleryMapper.toDto(distilleryRepository.findOne(id));
    }

    @Override
    public DistilleryDto update(long id, DistilleryDto updatedElement) {
        Distillery dist = distilleryRepository.update(id, distilleryMapper.toEntity(updatedElement));
        return distilleryMapper.toDto(dist);
    }

    public DistilleryDto save(DistilleryDto distilleryDto, List<Long> vineId){
        Distillery movie = distilleryRepository.save(distilleryMapper.toEntity(distilleryDto),vineId);
        return distilleryMapper.toDto(movie);
    }

    @Override
    public boolean remove(long id) {
        return distilleryRepository.remove(id);
    }
}
