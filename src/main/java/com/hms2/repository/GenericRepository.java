package com.hms2.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID extends Serializable> {
    
    T save(T entity);
    
    T update(T entity);
    
    void delete(T entity);
    
    void deleteById(ID id);
    
    Optional<T> findById(ID id);
    
    List<T> findAll();
    
    List<T> findAll(int page, int size);
    
    long count();
    
    boolean existsById(ID id);
}
