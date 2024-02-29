package net.bnijik.schooldbcli.dao.student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaStudentDao implements StudentDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final StudentQueries queries;

    @Autowired
    public JpaStudentDao(EntityManager entityManager, StudentQueries queries) {
        this.entityManager = entityManager;
        this.queries = queries;
    }

    @Override
    public Slice<Student> findAll(Pageable pageable) {
        TypedQuery<Student> query = entityManager.createQuery(queries.finaAll(), Student.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        List<Student> resultList = query.getResultList();

        boolean hasNext = resultList.size() > pageable.getPageSize();

        return new SliceImpl<>(resultList, pageable, hasNext);
    }

    @Override
    public long save(Student student) {
        entityManager.merge(student);
        return student.studentId();
    }

    @Override
    public Optional<Student> findById(long id) {
        return Optional.ofNullable(entityManager.find(Student.class, id));
    }

    @Override
    public boolean update(Student newStudent, long existingStudentId) {
        Student existingStudent = entityManager.getReference(Student.class, existingStudentId);
        if (null != existingStudent) {
            newStudent.studentId(existingStudentId);
            entityManager.merge(newStudent);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        Student student = entityManager.getReference(Student.class, id);
        if (null != student) {
            entityManager.remove(student);
            return true;
        }
        return false;
    }

    @Override
    public Slice<Student> findAllByCourseName(String courseName, Pageable pageable) {
        final TypedQuery<Student> query = entityManager.createQuery(queries.findAllByCourseName(), Student.class)
                .setParameter(StudentQueries.STUDENT_COURSE_NAME_PARAM, courseName)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize() + 1);

        final List<Student> resultList = query.getResultList();

        return new SliceImpl<>(resultList, pageable, resultList.size() > pageable.getPageSize());
    }

    @Transactional
    @Override
    public boolean enrollInCourses(long studentId, List<Long> courseIds) {
        final Student student = entityManager.getReference(Student.class, studentId);
        if (null == student) {
            return false;
        }

        courseIds.stream().map(id -> entityManager.getReference(Course.class, id)).forEach(student::addCourse);
        return true;
    }

    @Override
    public boolean withdrawFromCourse(long studentId, long courseId) {
        final Student student = entityManager.getReference(Student.class, studentId);
        if (null == student) {
            return false;
        }

        student.removeCourse(entityManager.getReference(Course.class, courseId));
        return true;
    }
}
