package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record CourseQueries(
        @Value("${course.find-all}") String finaAll,
        @Value("${course.find-all-for-student}") String findAllForStudent
) {

}
