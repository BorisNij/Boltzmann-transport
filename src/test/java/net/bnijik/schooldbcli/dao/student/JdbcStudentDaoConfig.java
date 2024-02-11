package net.bnijik.schooldbcli.dao.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class JdbcStudentDaoConfig {
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private StudentQueries queries;

    @Bean
    public JdbcStudentDao jdbcStudentDao() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template.getJdbcTemplate());
        return new JdbcStudentDao(template, insert, queries);
    }
}
