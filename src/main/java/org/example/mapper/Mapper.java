package org.example.mapper;
public interface Mapper<T, S> {
    S toDto(T company);
    T toEntity(S companyDto);
}
