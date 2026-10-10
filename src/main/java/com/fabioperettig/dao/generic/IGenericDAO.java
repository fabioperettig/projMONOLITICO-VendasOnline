package com.fabioperettig.dao.generic;


import com.fabioperettig.domain.Persistence;

import java.util.List;

public interface IGenericDAO<T extends Persistence> {

    T create(T entity);
    T read(Long id);
    T update(T entity);
    void delete(T entity);
    List<T> findAll();
}
