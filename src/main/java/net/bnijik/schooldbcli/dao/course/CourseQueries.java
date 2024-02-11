package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record CourseQueries(@Value("${course.find-by-id}") String findById,
                            @Value("${course.find-all}") String finaAll,
                            @Value("${course.find-all-for-student}") String findAllForStudent,
                            @Value("${course.update}") String update,
                            @Value("${course.delete-by-id}") String deleteById) {

    public static final String COURSE_ID_PARAM = "courseId";
    public static final String STUDENT_ID_PARAM = "studentId";
    public static final String COURSE_NAME_PARAM = "courseName";
    public static final String COURSE_DESCRIPTION_PARAM = "courseDescription";
    public static final String COURSE_ID_COLUMN = "course_id";
    public static final String COURSE_NAME_COLUMN = "course_name";
    public static final String COURSE_DESCRIPTION_COLUMN = "course_description";
    public static final String COURSE_TABLE_NAME = "courses";
}
