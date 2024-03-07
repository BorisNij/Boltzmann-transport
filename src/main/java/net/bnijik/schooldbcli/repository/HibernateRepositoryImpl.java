package net.bnijik.schooldbcli.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.AbstractSharedSessionContract;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

// Based on https://vladmihalcea.com/best-spring-data-jparepository/
public class HibernateRepositoryImpl<T> implements HibernateRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    public List<T> findAll() {
        throw new UnsupportedOperationException("Fetching all records from a given database table is not supported");
    }

    public <S extends T> S save(S entity) {
        return unsupportedSave();
    }

    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return unsupportedSave();
    }

    public <S extends T> S saveAndFlush(S entity) {
        return unsupportedSave();
    }

    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        return unsupportedSave();
    }

    public <S extends T> S persist(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    public <S extends T> S persistAndFlush(S entity) {
        persist(entity);
        entityManager.flush();
        return entity;
    }

    public <S extends T> List<S> persistAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::persist).toList();
    }

    public <S extends T> List<S> persistAllAndFlush(Iterable<S> entities) {
        return executeBatch(() -> {
            List<S> result = StreamSupport.stream(entities.spliterator(), false).map(this::persist).toList();
            entityManager.flush();
            return result;
        });
    }

    public <S extends T> S merge(S entity) {
        return entityManager.merge(entity);
    }

    public <S extends T> S mergeAndFlush(S entity) {
        S result = merge(entity);
        entityManager.flush();
        return result;
    }

    public <S extends T> List<S> mergeAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::merge).toList();
    }

    public <S extends T> List<S> mergeAllAndFlush(Iterable<S> entities) {
        return executeBatch(() -> {
            List<S> result = StreamSupport.stream(entities.spliterator(), false).map(this::merge).toList();
            entityManager.flush();
            return result;
        });
    }

    public <S extends T> S update(S entity) {
        //noinspection deprecation
        session().update(entity);
        return entity;
    }

    public <S extends T> S updateAndFlush(S entity) {
        update(entity);
        entityManager.flush();
        return entity;
    }

    public <S extends T> List<S> updateAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::update).toList();
    }

    public <S extends T> List<S> updateAllAndFlush(Iterable<S> entities) {
        return executeBatch(() -> {
            List<S> result = StreamSupport.stream(entities.spliterator(), false).map(this::update).toList();
            entityManager.flush();
            return result;
        });
    }

    protected Integer getBatchSize(Session session) {
        SessionFactoryImplementor sessionFactory = session.getSessionFactory().unwrap(SessionFactoryImplementor.class);

        final JdbcServices jdbcServices = sessionFactory.getServiceRegistry().getService(JdbcServices.class);

        if (!Objects.requireNonNull(jdbcServices).getExtractedMetaDataSupport().supportsBatchUpdates()) {
            return Integer.MIN_VALUE;
        }
        return session.unwrap(AbstractSharedSessionContract.class).getConfiguredJdbcBatchSize();
    }

    protected <R> R executeBatch(Supplier<R> callback) {
        Session session = session();
        Integer jdbcBatchSize = getBatchSize(session);
        Integer originalSessionBatchSize = session.getJdbcBatchSize();
        try {
            if (jdbcBatchSize == null) {
                session.setJdbcBatchSize(10);
            }
            return callback.get();
        } finally {
            session.setJdbcBatchSize(originalSessionBatchSize);
        }
    }

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    protected <S extends T> S unsupportedSave() {
        throw new UnsupportedOperationException("Use one of the merge methods or one of the persist methods instead");
    }
}