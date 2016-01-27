package com.semjournals.data.dao;

import com.semjournals.model.AbstractPersistentObject;

import java.util.List;

public interface DAO<T extends AbstractPersistentObject> {
    T create(T persistentObject);
    T get(String id);
    T get(String key, Object value);
    List<T> list();
    List<T> list(int offset, int maxSize);
    void update(T persistentObject);
//    T createOrUpdate(T persistentObject);
    void delete(T persistentObject);
}