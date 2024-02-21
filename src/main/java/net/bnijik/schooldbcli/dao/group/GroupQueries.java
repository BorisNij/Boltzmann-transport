package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record GroupQueries(
                           @Value("${group.find-by-name}")
                           String findByName,
                           @Value("${group.find-all}")
                           String finaAll,
                           @Value("${group.find-all-by-max-stud-count}")
                           String findAllByMaxStudCount
) {
}
