package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.entity.Course;
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

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JpaCourseDao.class, CourseQueries.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JpaCourseDaoTest {

    @Autowired
    private JpaCourseDao courseDao;

    @Test
    @DisplayName("when finding course by id should return the right course")
    void whenFindingCourseByIdShouldReturnTheRightCourse() {
        final Course expected = new Course(2L, "Course2", "Description2", new HashSet<>());
        final Optional<Course> optionalCourse = courseDao.findById(expected.courseId());

        assertThat(optionalCourse).contains(expected);
    }

    @Test
    @DisplayName("when saving course should save course")
    void whenSavingCourseOfCertainNameShouldSaveCourse() {
        Course fourthCourse = new Course(42344L, "Course3", "Course3 description", new HashSet<>());
        assertThat(courseDao.save(fourthCourse)).isEqualTo(4L);
    }

    @Test
    @DisplayName("when finding all courses page should return a list of all courses on page")
    void whenFindingAllCoursesPageShouldReturnAListOfAllCoursesOnPage() {
        final Slice<Course> courses = courseDao.findAll(PageRequest.of(0, 10));

        assertThat(courses).containsExactly(new Course(1L, "Course1", "Description1", new HashSet<>()),
                                            new Course(2L, "Course2", "Description2", new HashSet<>()),
                                            new Course(3L, "Course to delete", "Description", new HashSet<>()));

    }

    @Test
    @DisplayName("when deleting course by id should remove it from the database")
    void whenDeletingCourseByIdShouldRemoveFromDatabase() {
        assertThat(courseDao.delete(1L)).isTrue();
    }

    @Test
    @DisplayName("when updating existing course should update course")
    void whenUpdatingExistingCourseShouldUpdateCourse() {
        assertThat(courseDao.update(new Course(3L, "Modified course name", "Modified description", new HashSet<>()),
                                    3L)).isTrue();
    }

    @Test
    @DisplayName("when finding courses for a student should return the correct courses")
    void whenFindingCoursesForAStudentShouldReturnTheCorrectCourses() {
        Slice<Course> enrolledCourses = courseDao.findAllForStudent(1, PageRequest.of(0, 5));

        assertThat(enrolledCourses).containsExactly(new Course(1L, "Course1", "Description1", new HashSet<>()),
                                                    new Course(2L, "Course2", "Description2", new HashSet<>()));
    }


}