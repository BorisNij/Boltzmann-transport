package net.bnijik.schooldbcli.repository.group;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.repository.GroupRepository;
import net.bnijik.schooldbcli.repository.hibernate.HibernateRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collections;
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
    @DisplayName("when creating new group of certain name should return new group")
    void whenCreatingNewGroupOfCertainNameShouldReturnNewGroup() {
        final Group expected = new Group().groupName("AAA-11");

        final Group managedGroup = groupRepository.persist(expected);

        assertThat(expected).isEqualTo(managedGroup);
        assertThat(expected).isSameAs(managedGroup);
    }


    @Test
    @DisplayName("when finding a group by ID should return the correct group")
    void whenFindingAGroupByIdShouldReturnTheCorrectGroup() {
        Group expectedGroup = new Group().groupId(1L);

        Optional<Group> groupOptional = groupRepository.findById(expectedGroup.groupId());

        assertThat(groupOptional).hasValue(expectedGroup);
    }


    @Test
    @DisplayName("when finding a group by name should return the correct group")
    void whenFindingAGroupByNameShouldReturnTheCorrectGroup() {
        Group createdGroup = new Group().groupId(1L).groupName("BB-22");

        Optional<Group> groupOptional = groupRepository.findByGroupName("BB-22");

        assertThat(groupOptional).hasValue(createdGroup);
    }

    @Test
    @DisplayName("when finding all groups page should return slice of all groups")
    void whenFindingAllGroupsPageShouldReturnSliceOfAllGroups() {

        final Slice<Group> groupStream = groupRepository.findAll(PageRequest.of(0, 2));

        assertThat(groupStream).containsExactly(new Group(Collections.emptyList(), 1L, "BB-22"),
                                                new Group(Collections.emptyList(), 2L, "CC-33"));
    }

    @Test
    @DisplayName("when finding groups by max student count should return correct groups")
    void whenFindingGroupsByMaxStudentCountShouldReturnCorrectGroups() {
        Group group1 = new Group(Collections.emptyList(), 1, "BB-22");
        Group group2 = new Group(Collections.emptyList(), 2, "CC-33");
        Group group3 = new Group(Collections.emptyList(), 3, "Group to Delete");

        Slice<Group> groups = groupRepository.findAllByMaxStudentCount(1, PageRequest.of(0, 5));

        assertThat(groups).contains(group1, group3).doesNotContain(group2).hasSize(2);
    }

    @Test
    @DisplayName("when deleting a group should remove it from the database")
    void whenDeletingGroupShouldRemoveFromDatabase() {
        Group groupToDelete = new Group(new ArrayList<>(), 3, "Group to Delete");

        groupRepository.deleteById(groupToDelete.groupId());

        assertThat(entityManager.find(Group.class, groupToDelete.groupId())).isNull();

        entityManager.flush();
        entityManager.clear();
        assertThat(entityManager.find(Group.class, groupToDelete.groupId())).isNull();
    }

    @Test
    @DisplayName("when updating existing group should update group")
    void whenUpdatingExistingGroupShouldUpdateGroup() {
        final Group modifiedGroup = new Group().groupId(3L).groupName("Modified group name");
        final Group updated = groupRepository.update(modifiedGroup);
        assertThat(updated).isEqualTo(modifiedGroup);
        assertThat(updated).isSameAs(modifiedGroup);

        entityManager.flush();
        entityManager.clear();
        final Group group = entityManager.find(Group.class, modifiedGroup.groupId());
        assertThat(group.groupName()).isEqualTo(modifiedGroup.groupName());
    }

}