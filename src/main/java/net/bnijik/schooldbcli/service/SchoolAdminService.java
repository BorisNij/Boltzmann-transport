package net.bnijik.schooldbcli.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface SchoolAdminService<T> {
    Slice<T> findAll(Pageable page);

    long save(T t);

    Optional<T> findById(long id);

    boolean update(T t, long id);

    void delete(long id);
}
