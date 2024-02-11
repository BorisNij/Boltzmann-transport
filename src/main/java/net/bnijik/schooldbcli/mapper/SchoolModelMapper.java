package net.bnijik.schooldbcli.mapper;

import java.util.Collection;
import java.util.List;

public interface SchoolModelMapper<M, D> {
    D modelToDto(M model);

    M dtoToModel(D dto);

    List<D> modelsToDtos(Collection<M> models);

    List<M> dtosToModels(Collection<D> dtos);
}
