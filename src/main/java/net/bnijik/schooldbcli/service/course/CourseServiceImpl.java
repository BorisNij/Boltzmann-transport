package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.repository.CourseRepository;
import net.bnijik.schooldbcli.service.schoolAdmin.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends SchoolAdminServiceImpl<CourseDto, Course> implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        super(courseMapper, courseRepository);
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Slice<CourseDto> findAllForStudent(long studentId, Pageable page) {
        final Slice<Course> courses = courseRepository.findAllByStudentsStudentId(studentId, page);
        return courseMapper.modelsToDtos(courses);
    }

}
