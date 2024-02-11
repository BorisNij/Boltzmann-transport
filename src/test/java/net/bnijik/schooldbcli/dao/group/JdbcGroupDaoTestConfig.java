package net.bnijik.schooldbcli.dao.group;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class JdbcGroupDaoTestConfig {
    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private GroupQueries queries;

    @Bean
    public JdbcGroupDao jdbcGroupDao(HikariDataSource hikariDataSource) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(hikariDataSource);
        return new JdbcGroupDao(jdbcClient, insert, queries);
    }
}
