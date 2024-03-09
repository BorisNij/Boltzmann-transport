package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.entity.Course;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.repository.CourseRepository;
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
    @DisplayName("when successfully creating new course should return new course")
    void whenSuccessfullyCreatingNewCourseShouldReturnNewCourse(Course course, CourseDto courseDto) {
        when(courseRepository.persist(any(Course.class))).thenReturn(course);
        when(courseMapper.dtoToModel(any(CourseDto.class))).thenReturn(course);
        when(courseMapper.modelToDto(any(Course.class))).thenReturn(courseDto);

        final CourseDto created = courseService.create(courseDto);

        assertThat(created).isEqualTo(courseDto);
        assertThat(created).isSameAs(courseDto);
    }

    @ParameterizedTest
    @MethodSource("courseDtoProvider")
    @DisplayName("when finding all courses should return all courses")
    void whenFindingAllCoursesShouldReturnAllCourses(List<Course> courses, List<CourseDto> expected) {
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(courses));
        final PageImpl<CourseDto> expectedPageable = new PageImpl<>(expected);
        when(courseMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<CourseDto> actual = courseService.findAll(mock(Pageable.class));

        assertThat(actual).isEqualTo(expectedPageable);
        assertThat(actual).isSameAs(expectedPageable);
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when finding course by id should return the correct course")
    void whenFindingCourseByIdShouldReturnTheCorrectCourse(Course course, CourseDto courseDto) {

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseMapper.modelToDto(any(Course.class))).thenReturn(courseDto);

        final Optional<CourseDto> courseDtoOptional = courseService.findById(course.courseId());
        assertThat(courseDtoOptional).containsSame(courseDto);
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    @DisplayName("when updated course successfully should return updated course")
    void whenUpdatedCourseSuccessfullyShouldReturnUpdatedCourse(Course course, CourseDto courseDto) {
        when(courseRepository.update(any(Course.class))).thenReturn(course);
        when(courseMapper.dtoToModel(any(CourseDto.class))).thenReturn(course);
        when(courseMapper.modelToDto(any(Course.class))).thenReturn(courseDto);

        final CourseDto updated = courseService.update(courseDto);
        assertThat(updated).isEqualTo(courseDto);
        assertThat(updated).isSameAs(courseDto);
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
        final PageImpl<CourseDto> expectedPageable = new PageImpl<>(expected);
        when(courseMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<CourseDto> courseDtos = courseService.findAllForStudent(1, mock(Pageable.class));
        assertThat(courseDtos).isEqualTo(expectedPageable);
        assertThat(courseDtos).isSameAs(expectedPageable);
    }

}