package net.bnijik.schooldbcli.mapper;

import org.springframework.data.domain.Slice;

public interface SchoolModelMapper<M, D> {
    D modelToDto(M model);

    M dtoToModel(D dto);

    Slice<D> modelsToDtos(Iterable<M> models);

    Slice<M> dtosToModels(Iterable<D> dtos);
}
