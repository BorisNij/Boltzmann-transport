package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {CourseQueries.class, JdbcCourseDaoTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcCourseDaoTest {
    @Autowired
    private JdbcCourseDao courseDao;

    @Test
    @DisplayName("when finding course by id should return the right course")
    void whenFindingCourseByIdShouldReturnTheRightCourse() {
        final Course expected = new Course(2L, "Course2", "Description2");
        final Optional<Course> optionalCourse = courseDao.findById(expected.courseId());

        assertThat(optionalCourse).contains(expected);
    }


    @Test
    @DisplayName("when saving course should save course")
    void whenSavingCourseOfCertainNameShouldSaveCourse() {
        Course fourthCourse = new Course(42344L, "Course3", "Course3 description");
        assertThat(courseDao.save(fourthCourse)).isEqualTo(4L);
    }

    @Test
    @DisplayName("when finding all courses page should return a list of all courses on page")
    void whenFindingAllCoursesPageShouldReturnAListOfAllCoursesOnPage() {
        final List<Course> courses = courseDao.findAll(Page.of(1, 5));

        assertThat(courses).containsExactly(new Course(1L, "Course1", "Description1"),
                                            new Course(2L, "Course2", "Description2"),
                                            new Course(3L, "Course to delete", "Description"));

    }

    @Test
    @DisplayName("when deleting course by id should remove it from the database")
    void whenDeletingCourseByIdShouldRemoveFromDatabase() {
        assertThat(courseDao.delete(1L)).isTrue();
    }

    @Test
    @DisplayName("when updating existing course should update course")
    void whenUpdatingExistingCourseShouldUpdateCourse() {
        assertThat(courseDao.update(new Course(3L, "Modified course name", "Modified description"), 3L)).isTrue();
    }

    @Test
    @DisplayName("when finding courses for a student should return the correct courses")
    void whenFindingCoursesForAStudentShouldReturnTheCorrectCourses() {
        List<Course> enrolledCourses = courseDao.findAllForStudent(1, Page.of(1, 5));

        assertThat(enrolledCourses).containsExactly(new Course(1L, "Course1", "Description1"),
                                                    new Course(2L, "Course2", "Description2"));
    }
}