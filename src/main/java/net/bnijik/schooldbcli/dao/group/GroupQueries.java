package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record GroupQueries(@Value("${group.find-by-id}")
                           String findById,
                           @Value("${group.find-by-name}")
                           String findByName,
                           @Value("${group.find-all}")
                           String finaAll,
                           @Value("${group.find-all-by-max-stud-count}")
                           String findAllByMaxStudCount,
                           @Value("${group.update}")
                           String update,
                           @Value("${group.delete-by-id}")
                           String deleteById) {
    public static final String GROUP_ID_PARAM = "groupId";
    public static final String GROUP_NAME_PARAM = "groupName";
    public static final String GROUP_STUD_COUNT_PARAM = "studentCount";
    public static final String GROUP_ID_COLUMN = "group_id";
    public static final String GROUP_NAME_COLUMN = "group_name";
    public static final String GROUP_TABLE_NAME = "groups";
}
