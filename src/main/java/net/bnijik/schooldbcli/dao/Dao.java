package net.bnijik.schooldbcli.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface Dao<T> {
    Slice<T> findAll(Pageable page);

    long save(T t);

    Optional<T> findById(long id);

    boolean update(T t, long id);

    boolean delete(long id);
}
