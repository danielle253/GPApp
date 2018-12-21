package com.example.domis.android_app.repository;

import com.example.domis.android_app.model.Entity;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Repository {
    <T extends Entity> void getObject(String ref, String child, Consumer<T> consumer);
    <T extends Entity> void getObjectList(String ref, Consumer<List<T>> consumer);

    void add(String ref, String child, Object obj);
    void delete(String reference, String child);
    <T extends Entity> String push(String reference, T obj);
    <T extends Entity> void set(String reference, T obj);
    void update(String reference, Map obj);
}
