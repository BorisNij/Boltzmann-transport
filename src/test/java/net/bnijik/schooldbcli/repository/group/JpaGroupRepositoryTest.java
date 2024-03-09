package net.bnijik.schooldbcli.repository.group;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.bnijik.schooldbcli.entity.Group;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaRepositories(repositoryBaseClass = HibernateRepositoryImpl.class, basePackages = "net.bnijik.schooldbcli.repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JpaGroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("when saving group of certain name should save group")
    void whenSavingGroupOfCertainNameShouldSaveGroup() {

        final Group expected = new Group(new ArrayList<>(), 4, "AA-11");

        assertThat(expected).isEqualTo(groupRepository.merge(expected));
    }


    @Test
    @DisplayName("when finding a group by ID should return the correct group")
    void whenFindingAGroupByIdShouldReturnTheCorrectGroup() {
        Group expectedGroup = new Group(new ArrayList<>(), 1L, "BB-22");

        Optional<Group> groupOptional = groupRepository.findById(expectedGroup.groupId());

        assertThat(groupOptional).contains(expectedGroup);
    }


    @Test
    @DisplayName("when finding a group by name should return the correct group")
    void whenFindingAGroupByNameShouldReturnTheCorrectGroup() {
        Group createdGroup = new Group(new ArrayList<>(), 1, "BB-22");

        Optional<Group> groupOptional = groupRepository.findByGroupName("BB-22");

        assertThat(groupOptional).contains(createdGroup);
    }

    @Test
    @DisplayName("when finding all groups should return a stream of all groups")
    void whenFindingAllGroupsShouldReturnAStreamOfAllGroups() {

        final Slice<Group> groupStream = groupRepository.findAll(PageRequest.of(0, 5));

        assertThat(groupStream).containsExactly(new Group(new ArrayList<>(), 1L, "BB-22"),
                                                new Group(new ArrayList<>(), 2L, "CC-33"),
                                                new Group(new ArrayList<>(), 3L, "Group to Delete"));
    }

    @Test
    @DisplayName("when finding groups by max student count should return correct groups")
    void whenFindingGroupsByMaxStudentCountShouldReturnCorrectGroups() {
        Group group1 = new Group(new ArrayList<>(), 1, "BB-22");
        Group group2 = new Group(new ArrayList<>(), 2, "CC-33");
        Group group3 = new Group(new ArrayList<>(), 3, "Group to Delete");

        Slice<Group> groups = groupRepository.findAllByMaxStudentCount(1, PageRequest.of(0, 5));

        assertThat(groups).contains(group1, group3).doesNotContain(group2).hasSize(2);
    }

    @Test
    @DisplayName("when deleting a group should remove it from the database")
    void whenDeletingGroupShouldRemoveFromDatabase() {
        Group groupToDelete = new Group(new ArrayList<>(), 3, "Group to Delete");

        groupRepository.deleteById(groupToDelete.groupId());

        assertThat(entityManager.find(Group.class, groupToDelete.groupId())).isNull();
    }

    @Test
    @DisplayName("when updating existing group should update group")
    void whenUpdatingExistingGroupShouldUpdateGroup() {
        final Group modifiedGroupName = new Group(new ArrayList<>(), 3L, "Modified group name");

        assertThat(groupRepository.update(modifiedGroupName)).isEqualTo(modifiedGroupName);
    }

}