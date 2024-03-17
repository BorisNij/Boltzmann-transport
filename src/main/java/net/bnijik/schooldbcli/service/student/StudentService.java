package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.service.schoolAdmin.SchoolAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface StudentService extends SchoolAdminService<StudentDto> {

    Slice<StudentDto> findAllByCourseName(String courseName, Pageable pageable);

    boolean enrollInCourses(long studentId, List<Long> courseIds);

    boolean withdrawFromCourse(long studentId, long courseId);

    StudentDto create(String firstName, String lastName, long groupId);
}
