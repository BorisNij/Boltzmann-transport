package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.repository.course.CourseRepository;
import org.junit.jupiter.api.DisplayName;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseMapper courseMapper;

    @InjectMocks
    CourseServiceImpl courseService;

    private static Stream<Arguments> courseProvider() {
        return Stream.of(
                Arguments.of(
                        new Course(2L, "Some cool course", "Coolest description", new HashSet<>()),
                        new CourseDto(2L, "Some cool course", "Coolest description")
                ));
    }

    private static Stream<Arguments> courseDtoProvider() {
        List<String> strings = List.of("A", "B", "C");
        List<Course> courses = IntStream.range(0, strings.size())
                .mapToObj(i -> new Course(i, strings.get(i), strings.get(i), new HashSet<>()))
                .collect(Collectors.toList());
        List<CourseDto> dtos = IntStream.range(0, strings.size())
                .mapToObj(i -> new CourseDto(i, strings.get(i), strings.get(i)))
                .collect(Collectors.toList());

        return Stream.of(Arguments.of(courses, dtos));
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when successfully saving course should return new course id")
    void whenSuccessfullySavingCourseShouldReturnNewCourseId(Course course, CourseDto courseDto) {
        when(courseRepository.persist(any(Course.class))).thenReturn(course);
        when(courseMapper.dtoToModel(any(CourseDto.class))).thenReturn(course);

        final long newCourseId = courseService.save(courseDto);

        assertThat(newCourseId).isEqualTo(course.courseId());
    }

    @ParameterizedTest
    @MethodSource("courseDtoProvider")
    @DisplayName("when finding all courses should return all courses")
    void whenFindingAllCoursesShouldReturnAllCourses(List<Course> courses, List<CourseDto> expected) {
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(courses));
        when(courseMapper.modelsToDtos(any(Iterable.class))).thenReturn(new PageImpl<>(expected));

        final Slice<CourseDto> actual = courseService.findAll(mock(Pageable.class));

        assertThat(actual).isEqualTo(new PageImpl<>(expected));
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when finding course by id should return the correct course")
    void whenFindingCourseByIdShouldReturnTheCorrectCourse(Course course, CourseDto courseDto) {

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseMapper.modelToDto(any(Course.class))).thenReturn(courseDto);

        assertThat(courseService.findById(course.courseId())).contains(courseDto);
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when updated course successfully should return true")
    void whenUpdatedCourseSuccessfullyShouldReturnTrue(Course course, CourseDto courseDto) {
        when(courseRepository.update(any(Course.class))).thenReturn(course);
        when(courseMapper.dtoToModel(any(CourseDto.class))).thenReturn(course);
        when(courseMapper.modelToDto(any(Course.class))).thenReturn(courseDto);

        assertThat(courseService.update(courseDto, course.courseId())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when deleting course should use repo delete by id method")
    void whenDeletingCourseShouldUseRepoDeleteByIdMethod(Course course) {
        courseService.delete(course.courseId());
        verify(courseRepository, times(1)).deleteById(course.courseId());
    }

    @ParameterizedTest
    @MethodSource("courseDtoProvider")
    @DisplayName("when finding all courses for student should return all courses within page")
    void whenFindingAllCoursesForStudentShouldReturnAllCoursesWithinPage(List<Course> courses,
                                                                         List<CourseDto> expected) {

        when(courseRepository.findAllByStudentsStudentId(any(Long.class),
                                                         any(Pageable.class))).thenReturn(new PageImpl<>(courses));
        when(courseMapper.modelsToDtos(any(Iterable.class))).thenReturn(new PageImpl<>(expected));

        assertThat(courseService.findAllForStudent(1, mock(Pageable.class))).isEqualTo(new PageImpl<>(expected));
    }

}