package org.example.service.impl;

import org.example.dto.VineDto;
import org.example.entity.Vine;
import org.example.mapper.Mapper;
import org.example.mapper.impl.VineMapperImpl;
import org.example.repository.impl.VineRepository;
import org.example.service.Service;

import java.util.List;


public class VineService implements Service<VineDto> {
    private final VineRepository vineRepository;
    private final Mapper<Vine, VineDto> vineMapper;

    public VineService() {
        vineRepository = new VineRepository();
        vineMapper = new VineMapperImpl();
    }

    public VineService(VineRepository rep, VineMapperImpl map){
        this.vineRepository = rep;
        this.vineMapper = map;
    }

    @Override
    public List<VineDto> getAll() {
        List<Vine> actorList =vineRepository.findAll();
        return actorList.stream()
                .map(vineMapper::toDto)
                .toList();
    }

    @Override
    public VineDto getById(long id) {
        return vineMapper.toDto(vineRepository.findOne(id));
    }

    public VineDto save(VineDto actorDto) {
        Vine actor = vineRepository.save(vineMapper.toEntity(actorDto));
        return vineMapper.toDto(actor);
    }


    @Override
    public VineDto update(long id, VineDto updatedElement) {
        Vine actor = vineRepository.update(id, vineMapper.toEntity(updatedElement));
        return vineMapper.toDto(actor);
    }

    @Override
    public boolean remove(long id) {
        return vineRepository.remove(id);
    }
}
