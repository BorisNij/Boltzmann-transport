package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dao.course.CourseDao;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.model.Course;
import net.bnijik.schooldbcli.service.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends SchoolAdminServiceImpl<CourseDto, Course> implements CourseService {

    private final CourseDao courseDao;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao, CourseMapper courseMapper) {
        super(courseMapper, courseDao);
        this.courseDao = courseDao;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<CourseDto> findAllForStudent(long studentId, Page page) {
        final List<Course> courses = courseDao.findAllForStudent(studentId, page);
        return courseMapper.modelsToDtos(courses);
    }

}
