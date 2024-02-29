package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record StudentQueries(
        @Value("${student.find-all}")
        String finaAll,
        @Value("${student.find-all-by-course-name}")
        String findAllByCourseName
) {
    public static final String STUDENT_COURSE_NAME_PARAM = "courseName";
}
