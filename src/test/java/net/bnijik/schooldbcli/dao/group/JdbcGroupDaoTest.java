package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.entity.Group;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {GroupQueries.class, JdbcGroupDaoTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcGroupDaoTest {

    @Autowired
    private JdbcGroupDao groupDao;

    @Test
    @DisplayName("when saving group of certain name should save group")
    void whenSavingGroupOfCertainNameShouldSaveGroup() {

        final Group expected = new Group(4, "AA-11");

        assertThat(expected.groupId()).isEqualTo(groupDao.save(expected));
    }


    @Test
    @DisplayName("when finding a group by ID should return the correct group")
    void whenFindingAGroupByIdShouldReturnTheCorrectGroup() {
        Group expectedGroup = new Group(1L, "BB-22");

        Optional<Group> groupOptional = groupDao.findById(expectedGroup.groupId());

        assertThat(groupOptional).contains(expectedGroup);
    }


    @Test
    @DisplayName("when finding a group by name should return the correct group")
    void whenFindingAGroupByNameShouldReturnTheCorrectGroup() {
        Group createdGroup = new Group(1, "BB-22");

        Optional<Group> groupOptional = groupDao.findByName("BB-22");

        assertThat(groupOptional).contains(createdGroup);
    }

    @Test
    @DisplayName("when finding all groups should return a stream of all groups")
    void whenFindingAllGroupsShouldReturnAStreamOfAllGroups() {

        final List<Group> groupStream = groupDao.findAll(Page.of(1, 5));

        assertThat(groupStream).containsExactly(new Group(1L, "BB-22"),
                                                new Group(2L, "CC-33"),
                                                new Group(3L, "Group to Delete"));
    }

    @Test
    @DisplayName("when finding groups by max student count should return correct groups")
    void whenFindingGroupsByMaxStudentCountShouldReturnCorrectGroups() {
        Group group1 = new Group(1, "BB-22");
        Group group2 = new Group(2, "CC-33");
        Group group3 = new Group(3, "Group to Delete");

        List<Group> groups = groupDao.findAllByMaxStudentCount(1, Page.of(1, 5));

        assertThat(groups).contains(group1, group3).doesNotContain(group2).hasSize(2);
    }

    @Test
    @DisplayName("when deleting a group should remove it from the database")
    void whenDeletingGroupShouldRemoveFromDatabase() {
        Group groupToDelete = new Group(3, "Group to Delete");

        assertThat(groupDao.delete(groupToDelete.groupId())).isTrue();
    }

    @Test
    @DisplayName("when updating existing group should update group")
    void whenUpdatingExistingGroupShouldUpdateGroup() {
        assertThat(groupDao.update(new Group(3L, "Modified group name"), 3L)).isTrue();
    }

}