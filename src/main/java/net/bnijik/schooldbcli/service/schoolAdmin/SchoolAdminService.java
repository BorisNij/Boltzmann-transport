package net.bnijik.schooldbcli.service.schoolAdmin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface SchoolAdminService<T> {
    Slice<T> findAll(Pageable page);

    T create(T t);

    Optional<T> findById(long id);

    T update(T t);

    void delete(long id);
}
