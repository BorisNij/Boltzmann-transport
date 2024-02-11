package net.bnijik.schooldbcli.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll(Page page);

    long save(T t);

    Optional<T> findById(long id);

    boolean update(T t, long id);

    boolean delete(long id);
}
