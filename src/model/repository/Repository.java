package model.repository;

import java.util.List;

public interface Repository <T, K> {
    List<T> findAll();
    T findById(K id);
    T findByUuid(String uuid);
    Boolean save(T t);
    Boolean deleteById(K id);
    Boolean deleteByUuid(String uuid);
}
