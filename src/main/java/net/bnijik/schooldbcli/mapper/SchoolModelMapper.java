package net.bnijik.schooldbcli.mapper;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.StreamSupport;

public interface SchoolModelMapper<M, D> {
    D modelToDto(M model);

    M dtoToModel(D dto);

    default Slice<D> modelsToDtos(Iterable<M> models) {
        final List<D> dtos = StreamSupport.stream(models.spliterator(), false).map(this::modelToDto).toList();
        return new SliceImpl<>(dtos);
    }

    default Slice<M> dtosToModels(Iterable<D> dtos) {
        final List<M> models = StreamSupport.stream(dtos.spliterator(), false).map(this::dtoToModel).toList();
        return new SliceImpl<>(models);
    }
}
