package net.bnijik.schooldbcli.service.student;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dao.student.StudentDao;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.dto.StudentDto;
import net.bnijik.schooldbcli.mapper.StudentMapper;
import net.bnijik.schooldbcli.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    StudentDao studentDao;

    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentServiceImpl studentService;


    private static Stream<Arguments> studentProvider() {
        return Stream.of(Arguments.of(new Student(23L, 11L, "N1", "L1"),
                                      new StudentDto(23L,
                                                     new GroupDto(11L, "AA-11"),
                                                     "N1",
                                                     "L1",
                                                     List.of(new CourseDto(1L, "Math1", "Abcd"),
                                                             new CourseDto(2L, "Math2", "Abcd2")))));
    }

    private static Stream<Arguments> studentDtoProvider() {
        List<String> strings = List.of("A", "B", "C");
        List<Student> students = IntStream.range(0, strings.size())
                .mapToObj(i -> new Student(i, i, strings.get(i), strings.get(i)))
                .toList();
        List<StudentDto> dtos = IntStream.range(0, strings.size())
                .mapToObj(i -> new StudentDto(i,
                                              new GroupDto(i, strings.get(i)),
                                              strings.get(i),
                                              strings.get(i),
                                              List.of(new CourseDto(i, strings.get(i), strings.get(i)),
                                                      new CourseDto(i, strings.get(i), strings.get(i)))))
                .toList();

        return Stream.of(Arguments.of(students, dtos));
    }


    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when successfully saving student should return new student id")
    void whenSuccessfullySavingStudentShouldReturnNewStudentId(Student student, StudentDto studentDto) {
        when(studentDao.save(any(Student.class))).thenReturn(student.studentId());
        when(studentMapper.dtoToModel(any(StudentDto.class))).thenReturn(student);

        final long newStudentId = studentService.save(studentDto);

        assertThat(newStudentId).isEqualTo(student.studentId());
    }

    @ParameterizedTest
    @MethodSource("studentDtoProvider")
    @DisplayName("when finding all students should return all students")
    void whenFindingAllStudentsShouldReturnAllStudents(List<Student> students, List<StudentDto> expected) {
        when(studentDao.findAll(any(Page.class))).thenReturn(students);
        when(studentMapper.modelsToDtos(any(Collection.class))).thenReturn(expected);

        final List<StudentDto> actual = studentService.findAll(mock(Page.class));

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when finding student by id should return the correct student")
    void whenFindingStudentByIdShouldReturnTheCorrectStudent(Student student, StudentDto studentDto) {

        when(studentDao.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentMapper.modelToDto(any(Student.class))).thenReturn(studentDto);

        assertThat(studentService.findById(student.studentId())).contains(studentDto);
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when updated student successfully should return true")
    void whenUpdatedStudentSuccessfullyShouldReturnTrue(Student student, StudentDto studentDto) {
        when(studentDao.update(any(Student.class), any(Long.class))).thenReturn(true);
        when(studentMapper.dtoToModel(any(StudentDto.class))).thenReturn(student);

        assertThat(studentService.update(studentDto, student.studentId())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when successfully deleting student should return true")
    void whenSuccessfullyDeletingStudentShouldReturnTrue(Student student) {
        when(studentDao.delete(any(Long.class))).thenReturn(true);

        assertThat(studentService.delete(student.studentId())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("studentDtoProvider")
    @DisplayName("when finding all students enrolled in course should return right students")
    void whenFindingAllStudentsEnrolledInCourseShouldReturnRightStudents(List<Student> students,
                                                                         List<StudentDto> expected) {
        when(studentDao.findAllByCourseName(any(String.class), any(Page.class))).thenReturn(students);
        when(studentMapper.modelsToDtos(any(Collection.class))).thenReturn(expected);

        final List<StudentDto> actual = studentService.findAllByCourseName("course1", mock(Page.class));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("when successfully enrolled student in courses should return true")
    void whenSuccessfullyEnrolledStudentInCoursesShouldReturnTrue() {
        when(studentDao.enrollInCourses(any(Long.class), any(List.class))).thenReturn(true);

        assertThat(studentService.enrollInCourses(22L, Arrays.asList(1L, 2L, 3L, 4L))).isTrue();
    }

    @ParameterizedTest
    @MethodSource("studentProvider")
    @DisplayName("when successfully withdrew student from course should return true")
    void whenSuccessfullyWithdrewStudentFromCourseShouldReturnTrue(Student student) {
        when(studentDao.withdrawFromCourse(any(Long.class), any(Long.class))).thenReturn(true);

        assertThat(studentService.withdrawFromCourse(student.studentId(), 2L)).isTrue();
    }
}