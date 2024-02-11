package net.bnijik.schooldbcli.service;

import net.bnijik.schooldbcli.dao.Page;

import java.util.List;
import java.util.Optional;

public interface SchoolAdminService<T> {
    List<T> findAll(Page page);

    long save(T t);

    Optional<T> findById(long id);

    boolean update(T t, long id);

    boolean delete(long id);
}
