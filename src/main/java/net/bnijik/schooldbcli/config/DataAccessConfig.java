package net.bnijik.schooldbcli.config;

import com.zaxxer.hikari.HikariDataSource;
import net.bnijik.schooldbcli.repository.hibernate.HibernateRepositoryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = HibernateRepositoryImpl.class, basePackages = "net.bnijik.schooldbcli.repository")
public class DataAccessConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public DataSource hikariDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


}
