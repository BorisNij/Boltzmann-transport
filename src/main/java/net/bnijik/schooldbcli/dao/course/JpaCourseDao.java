package net.bnijik.schooldbcli.dao.course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import net.bnijik.schooldbcli.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseDao implements CourseDao {
    private final CourseQueries queries;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public JpaCourseDao(EntityManager entityManager, CourseQueries queries) {
        this.queries = queries;
        this.entityManager = entityManager;
    }


    @Override
    public Slice<Course> findAll(Pageable pageable) {
        TypedQuery<Course> query = entityManager.createQuery(queries.finaAll(), Course.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        List<Course> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    @Override
    public long save(Course course) {
        entityManager.merge(course);
        return course.courseId();
    }

    @Override
    public Optional<Course> findById(long id) {
        return Optional.ofNullable(entityManager.find(Course.class, id));
    }

    @Override
    public boolean update(Course newCourse, long existingCourseId) {
        Course existingCourse = entityManager.getReference(Course.class, existingCourseId);
        if (null != existingCourse) {
            newCourse.courseId(existingCourseId);
            entityManager.merge(newCourse);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        Course course = entityManager.getReference(Course.class, id);
        if (null != course) {
            entityManager.remove(course);
            return true;
        }
        return false;
    }

    @Override
    public Slice<Course> findAllForStudent(long studentId, Pageable pageable) {
        TypedQuery<Course> query = entityManager.createQuery(queries.findAllForStudent(), Course.class)
                .setParameter("studentId", studentId)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        List<Course> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        return new SliceImpl<>(resultList, pageable, hasNext);
    }
}
