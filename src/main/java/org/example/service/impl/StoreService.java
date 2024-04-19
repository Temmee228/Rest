package org.example.service.impl;

import org.example.dto.StoreDto;
import org.example.entity.Store;
import org.example.mapper.Mapper;
import org.example.mapper.impl.StoreMapperImpl;
import org.example.repository.impl.StoreRepository;
import org.example.service.Service;

import java.util.List;

public class StoreService implements Service<StoreDto> {
    private final StoreRepository storeRepository;
    private final Mapper<Store, StoreDto> storeMapper;

    public StoreService() {
        storeRepository = new StoreRepository();
        storeMapper = new StoreMapperImpl();
    }

    public StoreService(StoreRepository rep, StoreMapperImpl map){
        this.storeRepository = rep;
        this.storeMapper = map;
    }
    @Override
    public List<StoreDto> getAll() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(storeMapper::toDto).toList();
    }

    @Override
    public StoreDto getById(long id) {
        Store store = storeRepository.findOne(id);
        return storeMapper.toDto(store);
    }

    @Override
    public StoreDto update(long id, StoreDto updatedElement) {
        Store store = storeRepository.update(id, storeMapper.toEntity(updatedElement));
        return storeMapper.toDto(store);
    }

    public StoreDto save(StoreDto storeDto) {
        Store store = storeRepository.save(storeMapper.toEntity(storeDto));
        return storeMapper.toDto(store);
    }

    @Override
    public boolean remove(long id) {
        return storeRepository.remove(id);
    }
}
