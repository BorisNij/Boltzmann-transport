package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Slice<CourseDto> findAllForStudent(long studentId, Pageable page) {
        final Slice<Course> courses = courseRepository.findAllByStudentsStudentId(studentId, page);
        return courseMapper.modelsToDtos(courses);
    }

    @Override
    public Slice<CourseDto> findAll(Pageable page) {
        final Page<Course> courses = courseRepository.findAll(page);
        return courseMapper.modelsToDtos(courses);
    }

    @Override
    public long save(CourseDto courseDto) {
        final Course course = courseMapper.dtoToModel(courseDto);
        courseRepository.persist(course);
        return course.courseId();
    }

    @Override
    public Optional<CourseDto> findById(long id) {
        final Optional<Course> courseOptional = courseRepository.findById(id);
        return Optional.ofNullable(courseMapper.modelToDto(courseOptional.orElse(null)));
    }

    @Override
    public boolean update(CourseDto newCourseDto, long existingCourseId) {
        final CourseDto courseDto = new CourseDto(existingCourseId,
                                                  newCourseDto.courseName(),
                                                  newCourseDto.courseDescription());
        final Course updated = courseRepository.update(courseMapper.dtoToModel(courseDto));
        return courseMapper.modelToDto(updated).equals(courseDto);
    }

    @Override
    public void delete(long id) {
        courseRepository.deleteById(id);
    }
}
