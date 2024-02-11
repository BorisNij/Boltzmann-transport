package net.bnijik.schooldbcli.dao.course;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class JdbcCourseDaoTestConfig {
    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private CourseQueries queries;

    @Bean
    public JdbcCourseDao jdbcCourseDao(HikariDataSource hikariDataSource) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(hikariDataSource);
        return new JdbcCourseDao(insert, jdbcClient, queries);
    }
}
