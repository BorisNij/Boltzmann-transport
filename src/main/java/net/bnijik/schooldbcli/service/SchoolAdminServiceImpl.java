package net.bnijik.schooldbcli.service;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.mapper.SchoolModelMapper;

import java.util.List;
import java.util.Optional;

public class SchoolAdminServiceImpl<D, M> implements SchoolAdminService<D> {
    private final SchoolModelMapper<M, D> schoolModelMapper;
    private final Dao<M> dao;

    public SchoolAdminServiceImpl(SchoolModelMapper<M, D> schoolModelMapper, Dao<M> dao) {
        this.schoolModelMapper = schoolModelMapper;
        this.dao = dao;
    }

    @Override
    public List<D> findAll(Page page) {
        final List<M> all = dao.findAll(page);
        return schoolModelMapper.modelsToDtos(all);
    }

    @Override
    public long save(D d) {
        final M model = schoolModelMapper.dtoToModel(d);
        return dao.save(model);
    }

    @Override
    public Optional<D> findById(long id) {
        final Optional<M> modelOptional = dao.findById(id);
        return modelOptional.map(schoolModelMapper::modelToDto);
    }

    @Override
    public boolean update(D d, long id) {
        final M model = schoolModelMapper.dtoToModel(d);
        return dao.update(model, id);
    }

    @Override
    public boolean delete(long id) {
        return dao.delete(id);
    }
}
