package net.bnijik.schooldbcli.repository.course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.repository.CourseRepository;
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
class JpaCourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Test
    @DisplayName("when finding course by id should return the right course")
    void whenFindingCourseByIdShouldReturnTheRightCourse() {
        final Course expected = new Course(2L, "Course2", "Description2", Collections.emptySet());
        final Optional<Course> optionalCourse = courseRepository.findById(expected.courseId());

        assertThat(optionalCourse).hasValue(expected);
    }

    @Test
    @DisplayName("when creating new course should return new course")
    void whenCreatingNewCourseShouldReturnNewCourse() {
        final Course fourthCourse = new Course().courseName("Course3").courseDescription("Course3 description");

        final Course managedCourse = courseRepository.persist(fourthCourse);
        assertThat(managedCourse).isEqualTo(fourthCourse);
        assertThat(managedCourse).isSameAs(fourthCourse);
    }

    @Test
    @DisplayName("when finding all courses page should return a list of all courses on page")
    void whenFindingAllCoursesPageShouldReturnAListOfAllCoursesOnPage() {
        final Slice<Course> courses = courseRepository.findAll(PageRequest.of(0, 2));

        assertThat(courses).containsExactly(new Course(1L, "Course1", "Description1", Collections.emptySet()),
                                            new Course(2L, "Course2", "Description2", Collections.emptySet()));

    }

    @Test
    @DisplayName("when deleting course by id should remove it from the database")
    void whenDeletingCourseByIdShouldRemoveFromDatabase() {
        courseRepository.deleteById(3L);
        assertThat(entityManager.find(Course.class, 3L)).isNull();
    }

    @Test
    @DisplayName("when updating existing course should update course")
    void whenUpdatingExistingCourseShouldUpdateCourse() {
        final Course entity = new Course(3L, "Modified course name", "Modified description", Collections.emptySet());
        final Course updatedEntity = courseRepository.update(entity);
        assertThat(updatedEntity).isEqualTo(entity);
        assertThat(updatedEntity).isSameAs(entity);

        entityManager.flush();
        final Course course = entityManager.find(Course.class, entity.courseId());
        assertThat(course.courseName()).isEqualTo(updatedEntity.courseName());
        assertThat(course.courseDescription()).isEqualTo(updatedEntity.courseDescription());
        assertThat(course).isSameAs(entity);
    }

    @Test
    @DisplayName("when finding courses for a student should return the correct courses")
    void whenFindingCoursesForAStudentShouldReturnTheCorrectCourses() {
        Slice<Course> enrolledCourses = courseRepository.findAllByStudentsStudentId(1, PageRequest.of(0, 5));

        assertThat(enrolledCourses).containsExactly(new Course(1L, "Course1", "Description1", Collections.emptySet()),
                                                    new Course(2L, "Course2", "Description2", Collections.emptySet()));
    }


}