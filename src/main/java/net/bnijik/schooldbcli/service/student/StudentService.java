package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.service.SchoolAdminService;

import java.util.List;

public interface StudentService extends SchoolAdminService<StudentDto> {

    List<StudentDto> findAllByCourseName(String courseName, Page page);

    boolean enrollInCourses(long studentId, List<Long> courseIds);

    boolean withdrawFromCourse(long studentId, long courseId);

    long save(String firstName, String lastName, long groupId);
}
