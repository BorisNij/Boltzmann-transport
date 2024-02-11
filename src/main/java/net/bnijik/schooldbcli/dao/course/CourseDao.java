package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;

import java.util.List;

public interface CourseDao extends Dao<Course> {
    List<Course> findAllForStudent(long studentId, Page page);
}
