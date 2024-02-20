package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CourseDao extends Dao<Course> {
    Slice<Course> findAllForStudent(long studentId, Pageable pageable);
}
