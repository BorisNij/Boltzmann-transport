package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.service.SchoolAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CourseService extends SchoolAdminService<CourseDto> {

    Slice<CourseDto> findAllForStudent(long studentId, Pageable page);
}
