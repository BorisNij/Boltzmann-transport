package net.bnijik.schooldbcli.repository;

import net.bnijik.schooldbcli.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends SchoolRepository<Course> {
    Slice<Course> findAllByStudentsStudentId(long studentId, Pageable pageable);
}
