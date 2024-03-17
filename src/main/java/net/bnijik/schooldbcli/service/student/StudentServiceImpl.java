package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.entity.Student;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import net.bnijik.schooldbcli.mapper.StudentMapper;
import net.bnijik.schooldbcli.repository.CourseRepository;
import net.bnijik.schooldbcli.repository.StudentRepository;
import net.bnijik.schooldbcli.service.group.GroupService;
import net.bnijik.schooldbcli.service.schoolAdmin.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl extends SchoolAdminServiceImpl<StudentDto, Student> implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper,
                              StudentRepository studentRepository,
                              CourseRepository courseRepository,
                              GroupService groupService,
                              GroupMapper groupMapper) {
        super(studentMapper, studentRepository);
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @Override
    public Slice<StudentDto> findAllByCourseName(String courseName, Pageable pageable) {
        final Slice<Student> students = studentRepository.findAllByCoursesCourseName(courseName, pageable);
        return studentMapper.modelsToDtos(students);
    }

    @Override
    public boolean enrollInCourses(long studentId, final List<Long> courseIds) {
        if (CollectionUtils.isEmpty(courseIds)) {
            return true;
        }

        if (courseIds.stream().anyMatch(Objects::isNull)) {
            //TODO: throw exception custom instead
            return false;
        }

        final Optional<Student> studentOptional = studentRepository.findByIdWithCourses(studentId);

        if (studentOptional.isEmpty()) {
            //TODO: throw custom exception instead
            return false;
        }
        final Student student = studentOptional.get();

        final List<Course> courses = courseIds.stream().map(courseRepository::getReferenceById).toList();

        if (CollectionUtils.isEmpty(courses)) {
            return true;
        }

        student.addCourses(courses);
        return true;
    }

    @Override
    public boolean withdrawFromCourse(long studentId, long courseId) {
        final Course courseProxyWithId = courseRepository.getReferenceById(courseId);

        final Optional<Student> studentOptional = studentRepository.findByIdWithCourses(studentId);
        if (studentOptional.isEmpty()) {
            //TODO: throw custom exception instead
            return false;
        }

        studentOptional.get().removeCourse(courseProxyWithId);
        return true;
    }

    @Override
    public StudentDto create(String firstName, String lastName, long groupId) {
        final GroupDto groupDto = groupService.findById(groupId).orElse(null);
        final Group group = groupMapper.dtoToModel(groupDto);
        return super.create(studentMapper.modelToDto(new Student().firstName(firstName)
                                                             .lastName(lastName)
                                                             .group(group)));
    }
}
