package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JpaStudentDao.class, StudentQueries.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JpaStudentDaoTest {

    @Autowired
    private JpaStudentDao studentDao;

    @Test
    @DisplayName("when saving student should save student")
    void whenSavingStudentShouldSaveStudent() {

        Student expectedStudent = new Student(234,
                                              new Group(),
                                              "John",
                                              "Doe",
                                              Collections.emptySet());

        assertThat(studentDao.save(expectedStudent)).isEqualTo(4);
    }

    @Test
    @DisplayName("when finding all students should return stream of all students")
    void whenFindingAllStudentsShouldReturnStreamOfAllStudents() {

        final Slice<Student> studentStream = studentDao.findAll(PageRequest.of(0, 5));

        assertThat(studentStream).containsExactly(new Student(1L, new Group(), "Jane", "Doe", Collections.emptySet()),
                                                  new Student(2L,
                                                              new Group(),
                                                              "Student",
                                                              "ToRemove",
                                                              Collections.emptySet()),
                                                  new Student(3L,
                                                              new Group(),
                                                              "Student2",
                                                              "McStudent2",
                                                              Collections.emptySet()));
    }

    @Test
    @DisplayName("when finding student by id should return the correct student")
    void whenFindingStudentByIdShouldReturnTheCorrectStudent() {
        Student expectedStudent = new Student(1L, new Group(), "Jane", "Doe", Collections.emptySet());
        Optional<Student> studentOptional = studentDao.findById(expectedStudent.studentId());

        assertThat(studentOptional).hasValue(expectedStudent);
    }

    @Test
    @DisplayName("when updating existing student should update student")
    void whenUpdatingExistingStudentShouldUpdateStudent() {
        assertThat(studentDao.update(new Student(3L,
                                                 new Group(),
                                                 "Student2ModName",
                                                 "McStudent2",
                                                 Collections.emptySet()), 3L)).isTrue();
    }

    @Test
    @DisplayName("when deleting a student should remove it from the database")
    void whenDeletingAStudentShouldRemoveItFromTheDatabase() {
        final Student studentToRemove = new Student(2, new Group(), "Student", "ToRemove", Collections.emptySet());
        assertThat(studentDao.delete(studentToRemove.studentId())).isTrue();
    }

    @Test
    @DisplayName("when finding all students enrolled in given course should return correct students")
    void whenFindingAllStudentsEnrolledInGivenCourseShouldReturnCorrectStudents() {
        final Student student1 = new Student(1, new Group(), "Jane", "Doe", Collections.emptySet());
        final Student student2 = new Student(2, new Group(), "Student", "ToRemove", Collections.emptySet());
        final Course course2 = new Course(2, "Course2", "Description2", Collections.emptySet());

        final Slice<Student> students = studentDao.findAllByCourseName(course2.courseName(), PageRequest.of(0, 5));

        assertThat(students).containsExactly(student1, student2);
    }

    @Test
    @DisplayName("when enrolling a student in courses should update the student's enrollment")
    void whenEnrollingStudentInCoursesShouldUpdateEnrollment() {
        Student existingStudent = new Student(3, new Group(), "Student2", "McStudent2", Collections.emptySet());

        String course1Name = "Course1";
        String course1Description = "Description1";
        String course2Name = "Course2";
        String course2Description = "Description2";
        Course course1 = new Course(1, course1Name, course1Description, Collections.emptySet());
        Course course2 = new Course(2, course2Name, course2Description, Collections.emptySet());

        assertThat(studentDao.enrollInCourses(existingStudent.studentId(),
                                              Arrays.asList(course1.courseId(), course2.courseId()))).isTrue();

    }

    @Test
    @DisplayName("when withdrawing a student from a course should update the student's enrollment")
    void whenWithdrawingStudentFromCourseShouldUpdateEnrollment() {

        Student createdStudent = new Student(1, new Group(), "Jane", "Doe", Collections.emptySet());
        Course courseToWithdraw = new Course(1, "Course1", "Description1", new HashSet<>());

        assertThat(studentDao.withdrawFromCourse(createdStudent.studentId(), courseToWithdraw.courseId())).isTrue();
    }
}