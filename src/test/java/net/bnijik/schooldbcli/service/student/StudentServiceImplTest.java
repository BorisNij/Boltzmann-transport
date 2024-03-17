package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dto.CourseDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    StudentMapper studentMapper;

    @Mock
    GroupService groupService;

    @Mock
    GroupMapper groupMapper;

    @InjectMocks
    StudentServiceImpl studentService;


    private static Stream<Arguments> studentProvider() {
        return Stream.of(Arguments.of(new Student(23L, new Group(), "N1", "L1", Collections.emptySortedSet()),
                                      new StudentDto(23L,
                                                     new GroupDto(11L, "AA-11"),
                                                     "N1",
                                                     "L1",
                                                     new SliceImpl<>(List.of(new CourseDto(1L, "Math1", "Abcd"),
                                                                             new CourseDto(2L, "Math2", "Abcd2"))))));
    }

    private static Stream<Arguments> studentDtoProvider() {
        List<String> strings = List.of("A", "B", "C");
        List<Student> students = IntStream.range(0, strings.size())
                .mapToObj(i -> new Student(i,
                                           new Group(),
                                           strings.get(i),
                                           strings.get(i),
                                           Collections.emptySortedSet()))
                .toList();
        List<StudentDto> dtos = IntStream.range(0, strings.size())
                .mapToObj(i -> new StudentDto(i,
                                              new GroupDto(i, strings.get(i)),
                                              strings.get(i),
                                              strings.get(i),
                                              new SliceImpl<>(List.of(new CourseDto(i, strings.get(i), strings.get(i)),
                                                                      new CourseDto(i,
                                                                                    strings.get(i),
                                                                                    strings.get(i))))))
                .toList();

        return Stream.of(Arguments.of(students, dtos));
    }


    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when successfully creating new student should return new student")
    void whenSuccessfullyCreatingNewStudentShouldReturnNewStudent(Student student, StudentDto studentDto) {
        when(studentRepository.persist(any(Student.class))).thenReturn(student);
        when(studentMapper.dtoToModel(any(StudentDto.class))).thenReturn(student);
        when(studentMapper.modelToDto(any(Student.class))).thenReturn(studentDto);
        when(groupMapper.dtoToModel(any(GroupDto.class))).thenReturn(student.group());
        when(groupService.findById(any(Long.class))).thenReturn(Optional.of(studentDto.group()));

        final StudentDto created = studentService.create(studentDto.firstName(),
                                                         studentDto.lastName(),
                                                         studentDto.group().groupId());

        assertThat(created).isEqualTo(studentDto);
        assertThat(created).isSameAs(studentDto);
    }

    @ParameterizedTest
    @MethodSource("studentDtoProvider")
    @DisplayName("when finding all students should return all students")
    void whenFindingAllStudentsShouldReturnAllStudents(List<Student> students, List<StudentDto> expected) {
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(students));
        final SliceImpl<StudentDto> expectedPageable = new SliceImpl<>(expected);
        when(studentMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<StudentDto> actual = studentService.findAll(mock(Pageable.class));

        assertThat(actual).isEqualTo(expectedPageable);
        assertThat(actual).isSameAs(expectedPageable);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when finding student by id should return the correct student")
    void whenFindingStudentByIdShouldReturnTheCorrectStudent(Student student, StudentDto studentDto) {

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentMapper.modelToDto(any(Student.class))).thenReturn(studentDto);

        assertThat(studentService.findById(student.studentId())).contains(studentDto);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when updated student successfully should return updated student")
    void whenUpdatedStudentSuccessfullyShouldReturnUpdatedStudent(Student student, StudentDto studentDto) {
        when(studentRepository.update(any(Student.class))).thenReturn(student);
        when(studentMapper.dtoToModel(any(StudentDto.class))).thenReturn(student);
        when(studentMapper.modelToDto(any(Student.class))).thenReturn(studentDto);

        final StudentDto updated = studentService.update(studentDto);
        assertThat(updated).isSameAs(studentDto);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when deleting student should use repo delete by id method")
    void whenDeletingStudentShouldUseRepoDeleteByIdMethod(Student student) {
        studentService.delete(student.studentId());
        verify(studentRepository, times(1)).deleteById(student.studentId());
    }

    @ParameterizedTest
    @MethodSource("studentDtoProvider")
    @DisplayName("when finding all students enrolled in course should return right students")
    void whenFindingAllStudentsEnrolledInCourseShouldReturnRightStudents(List<Student> students,
                                                                         List<StudentDto> expected) {
        when(studentRepository.findAllByCoursesCourseName(any(String.class),
                                                          any(Pageable.class))).thenReturn(new SliceImpl<>(students));
        final SliceImpl<StudentDto> expectedPageable = new SliceImpl<>(expected);
        when(studentMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<StudentDto> actual = studentService.findAllByCourseName("course1", mock(Pageable.class));

        assertThat(actual).isEqualTo(expectedPageable);
        assertThat(actual).isSameAs(expectedPageable);
    }

    @Test
    @DisplayName("when successfully enrolled student in courses should return true")
    void whenSuccessfullyEnrolledStudentInCoursesShouldReturnTrue() {
        final Student student = new Student(3L,
                                            new Group().groupName("CC-33"),
                                            "Student2",
                                            "McStudent2",
                                            new TreeSet<>());
        when(studentRepository.findByIdWithCourses(3L)).thenReturn(Optional.of(student));
        when(courseRepository.getReferenceById(1L)).thenReturn(new Course().courseId(1L));
        when(courseRepository.getReferenceById(2L)).thenReturn(new Course().courseId(2L));

        assertThat(studentService.enrollInCourses(3L, Arrays.asList(1L, 2L))).isTrue();

    }

    @Test
    @DisplayName("when successfully withdrew student from course should return true")
    void whenSuccessfullyWithdrewStudentFromCourseShouldReturnTrue() {
        when(courseRepository.getReferenceById(1L)).thenReturn(new Course().courseId(1L));
        final Student student = new Student(1L,
                                            new Group().groupName("BB-22"),
                                            "Jane",
                                            "Doe",
                                            new TreeSet<>(List.of(new Course().courseId(1).courseName("Course1"),
                                                                  new Course().courseId(2).courseName("Course2"))));
        when(studentRepository.findByIdWithCourses(1L)).thenReturn(Optional.of(student));

        assertThat(studentService.withdrawFromCourse(student.studentId(), 1L)).isTrue();
    }
}