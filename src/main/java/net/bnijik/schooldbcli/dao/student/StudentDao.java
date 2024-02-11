package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> findAllByCourseName(String courseName, Page page);

    boolean enrollInCourses(long studentId, List<Long> courseIds);

    boolean withdrawFromCourse(long studentId, long courseId);

}
