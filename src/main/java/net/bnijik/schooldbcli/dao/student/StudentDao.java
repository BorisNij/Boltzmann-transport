package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    Slice<Student> findAllByCourseName(String courseName, Pageable pageable);

    boolean enrollInCourses(long studentId, List<Long> courseIds);

    boolean withdrawFromCourse(long studentId, long courseId);

}
