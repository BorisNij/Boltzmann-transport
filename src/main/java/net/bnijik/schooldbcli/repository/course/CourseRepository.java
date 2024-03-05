package net.bnijik.schooldbcli.repository.course;

import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.repository.HibernateRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, HibernateRepository<Course> {
    Slice<Course> findAllByStudentsStudentId(long studentId, Pageable pageable);
}
