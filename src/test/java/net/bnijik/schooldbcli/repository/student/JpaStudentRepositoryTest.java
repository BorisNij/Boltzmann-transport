package net.bnijik.schooldbcli.repository.student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.entity.Student;
import net.bnijik.schooldbcli.repository.StudentRepository;
import net.bnijik.schooldbcli.repository.hibernate.HibernateRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaRepositories(repositoryBaseClass = HibernateRepositoryImpl.class, basePackages = "net.bnijik.schooldbcli.repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JpaStudentRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("when creating new student should return new student")
    void whenCreatingNewStudentShouldReturnNewStudent() {

        Student expectedStudent = new Student().firstName("John").lastName("Doe");

        assertThat(studentRepository.persist(expectedStudent)).isEqualTo(expectedStudent);
    }

    @Test
    @DisplayName("when finding all students should return slice of all students")
    void whenFindingAllStudentsShouldReturnSliceOfAllStudents() {

        final Slice<Student> studentStream = studentRepository.findAll(PageRequest.of(0, 5));

        assertThat(studentStream).containsExactly(new Student(1L,
                                                              new Group(),
                                                              "Jane",
                                                              "Doe",
                                                              Collections.emptySortedSet()),
                                                  new Student(2L,
                                                              new Group(),
                                                              "Student",
                                                              "ToRemove",
                                                              Collections.emptySortedSet()),
                                                  new Student(3L,
                                                              new Group(),
                                                              "Student2",
                                                              "McStudent2",
                                                              Collections.emptySortedSet()));
    }

    @Test
    @DisplayName("when finding student by id should return the correct student")
    void whenFindingStudentByIdShouldReturnTheCorrectStudent() {
        Student expectedStudent = new Student().studentId(1L);
        Optional<Student> studentOptional = studentRepository.findById(expectedStudent.studentId());

        assertThat(studentOptional).hasValue(expectedStudent);
    }

    @Test
    @DisplayName("when updating existing student should update student")
    void whenUpdatingExistingStudentShouldUpdateStudent() {
        final Student modifiedStudent = new Student().studentId(3L).firstName("Student2ModName").lastName("McStudent2");
        final Student updated = studentRepository.update(modifiedStudent);
        assertThat(updated).isEqualTo(modifiedStudent);
        assertThat(updated).isSameAs(modifiedStudent);

        entityManager.flush();
        entityManager.clear();
        final Student student = entityManager.find((Student.class), modifiedStudent.studentId());
        assertThat(student.firstName()).isEqualTo(modifiedStudent.firstName());
    }

    @Test
    @DisplayName("when deleting a student should remove it from the database")
    void whenDeletingAStudentShouldRemoveItFromTheDatabase() {
        final Student studentToRemove = new Student().studentId(2L);
        studentRepository.deleteById(studentToRemove.studentId());

        assertThat(entityManager.find(Student.class, studentToRemove.studentId())).isNull();

        entityManager.flush();
        entityManager.clear();
        assertThat(entityManager.find(Student.class, studentToRemove.studentId())).isNull();
    }

    @Test
    @DisplayName("when finding all students enrolled in given course should return correct students")
    void whenFindingAllStudentsEnrolledInGivenCourseShouldReturnCorrectStudents() {
        final Student student1 = new Student().studentId(1L);
        final Student student2 = new Student().studentId(2L);
        final Course course2 = new Course().courseName("Course2");

        final Slice<Student> students = studentRepository.findAllByCoursesCourseName(course2.courseName(),
                                                                                     PageRequest.of(0, 5));

        assertThat(students).containsExactly(student1, student2);
    }




}