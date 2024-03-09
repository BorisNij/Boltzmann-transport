package net.bnijik.schooldbcli.service.schoolAdmin;

import net.bnijik.schooldbcli.mapper.SchoolModelMapper;
import net.bnijik.schooldbcli.repository.SchoolRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public class SchoolAdminServiceImpl<D, M> implements SchoolAdminService<D> {
    private final SchoolModelMapper<M, D> schoolModelMapper;
    private final SchoolRepository<M> schoolRepository;

    public SchoolAdminServiceImpl(SchoolModelMapper<M, D> schoolModelMapper, SchoolRepository<M> schoolRepository) {
        this.schoolModelMapper = schoolModelMapper;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public Slice<D> findAll(Pageable page) {
        final Slice<M> all = schoolRepository.findAll(page);
        return schoolModelMapper.modelsToDtos(all);
    }

    @Override
    public D create(D d) {
        final M model = schoolModelMapper.dtoToModel(d);
        final M persistedModel = schoolRepository.persist(model);
        return schoolModelMapper.modelToDto(persistedModel);
    }

    @Override
    public Optional<D> findById(long id) {
        final Optional<M> modelOptional = schoolRepository.findById(id);
        return modelOptional.map(schoolModelMapper::modelToDto);
    }

    @Override
    public D update(D d) {
        final M model = schoolModelMapper.dtoToModel(d);
        return schoolModelMapper.modelToDto(schoolRepository.update(model));
    }

    @Override
    public void delete(long id) {
        schoolRepository.deleteById(id);
    }
}
