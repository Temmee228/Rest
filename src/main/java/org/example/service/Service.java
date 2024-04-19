package org.example.service;

import java.util.List;

public interface Service<T> {
    List<T> getAll();

    T getById(long id);

    T update(long id, T updatedElement);

    boolean remove(long id);
}
