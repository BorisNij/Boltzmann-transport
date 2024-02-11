package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.service.SchoolAdminService;

import java.util.List;

public interface CourseService extends SchoolAdminService<CourseDto> {

    List<CourseDto> findAllForStudent(long studentId, Page page);
}
