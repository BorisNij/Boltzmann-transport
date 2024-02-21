package net.bnijik.schooldbcli.dao.group;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import net.bnijik.schooldbcli.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGroupDao implements GroupDao {
    private final GroupQueries queries;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public JpaGroupDao(EntityManager entityManager, GroupQueries queries) {
        this.entityManager = entityManager;
        this.queries = queries;
    }

    @Override
    public long save(Group group) {
        entityManager.merge(group);
        return group.groupId();
    }

    @Override
    public Optional<Group> findById(long id) {
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }

    @Override
    public Slice<Group> findAll(Pageable pageable) {
        TypedQuery<Group> query = entityManager.createQuery(queries.finaAll(), Group.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        List<Group> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    @Override
    public Optional<Group> findByName(String name) {
        return Optional.ofNullable(entityManager.createQuery(queries.findByName(), Group.class)
                                           .setParameter("groupName", name).getSingleResult());

    }

    @Override
    public Slice<Group> findAllByMaxStudentCount(int maxStudentCount, Pageable pageable) {
        TypedQuery<Group> query = entityManager.createQuery(queries.findAllByMaxStudCount(), Group.class)
                .setParameter("studentCount", maxStudentCount)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        List<Group> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    @Override
    public boolean update(Group newGroup, long existingGroupId) {
        Group existingGroup = entityManager.getReference(Group.class, existingGroupId);
        if (null != existingGroup) {
            newGroup.groupId(existingGroupId);
            entityManager.merge(newGroup);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        Group group = entityManager.getReference(Group.class, id);
        if (null != group) {
            entityManager.remove(group);
            return true;
        }
        return false;
    }
}
