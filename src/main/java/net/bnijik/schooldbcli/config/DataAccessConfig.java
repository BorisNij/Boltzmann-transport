package net.bnijik.schooldbcli.config;

import com.zaxxer.hikari.HikariDataSource;
import net.bnijik.schooldbcli.repository.hibernate.HibernateRepositoryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = HibernateRepositoryImpl.class, basePackages = "net.bnijik.schooldbcli.repository")
public class DataAccessConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public HikariDataSource hikariDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public SimpleJdbcInsert simpleJdbcInsert(HikariDataSource hikariDataSource) {
        return new SimpleJdbcInsert(hikariDataSource);
    }

}
