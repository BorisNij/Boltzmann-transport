package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record StudentQueries(
        @Value("${student.find-by-id}")
        String findById,
        @Value("${student.find-all}")
        String finaAll,
        @Value("${student.find-all-by-course-name}")
        String findAllByCourseName,
        @Value("${student.update}")
        String update,
        @Value("${student.enroll-in-courses}")
        String enrollInCourses,
        @Value("${student.withdraw-from-course}")
        String withdrawFromCourse,
        @Value("${student.delete-by-id}")
        String deleteById
) {
    public static final String STUDENT_ID_PARAM = "studentId";
    public static final String STUDENT_FIRST_NAME_PARAM = "firstName";
    public static final String STUDENT_LAST_NAME_PARAM = "lastName";
    public static final String STUDENT_GROUP_ID_PARAM = "groupId";
    public static final String STUDENT_COURSE_NAME_PARAM = "courseName";
    public static final String STUDENT_COURSE_ID_PARAM = "courseId";
    public static final String STUDENT_ID_COLUMN = "student_id";
    public static final String STUDENT_FIRST_NAME_COLUMN = "first_name";
    public static final String STUDENT_LAST_NAME_COLUMN = "last_name";
    public static final String STUDENT_GROUP_ID_COLUMN = "group_id";
    public static final String STUDENT_TABLE_NAME = "students";
}
