package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dao.group.GroupDao;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupDao groupDao;

    @Mock
    GroupMapper groupMapper;

    @InjectMocks
    GroupServiceImpl groupService;


    private static Stream<Arguments> groupProvider() {
        return Stream.of(Arguments.of(new Group(new ArrayList<>(), 23L, "Some cool group"),
                                      new net.bnijik.schooldbcli.dto.GroupDto(23L, "Some cool group")));
    }

    private static Stream<Arguments> groupDtoProvider() {
        List<String> strings = List.of("A", "B", "C");
        List<Group> groups = IntStream.range(0, strings.size())
                .mapToObj(i -> new Group(new ArrayList<>(), i, strings.get(i)))
                .toList();
        List<GroupDto> dtos = IntStream.range(0, strings.size())
                .mapToObj(i -> new GroupDto(i, strings.get(i)))
                .toList();

        return Stream.of(Arguments.of(groups, dtos));
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when successfully saving group should return new group id")
    void whenSuccessfullySavingGroupShouldReturnNewGroupId(Group group, GroupDto groupDto) {
        when(groupDao.save(any(Group.class))).thenReturn(group.groupId());
        when(groupMapper.dtoToModel(any(GroupDto.class))).thenReturn(group);

        final long newGroupId = groupService.save(groupDto);

        assertThat(newGroupId).isEqualTo(group.groupId());
    }

    @ParameterizedTest
    @MethodSource("groupDtoProvider")
    @DisplayName("when finding all groups should return all groups")
    void whenFindingAllGroupsShouldReturnAllGroups(List<Group> groups, List<GroupDto> expected) {
        when(groupDao.findAll(any(Pageable.class))).thenReturn(new SliceImpl<>(groups));
        when(groupMapper.modelsToDtos(any(Iterable.class))).thenReturn(new SliceImpl<>(expected));

        final Slice<GroupDto> actual = groupService.findAll(mock(Pageable.class));

        assertThat(actual).isEqualTo(new SliceImpl<>(expected));
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when finding group by id should return the correct group")
    void whenFindingGroupByIdShouldReturnTheCorrectGroup(Group group, GroupDto groupDto) {

        when(groupDao.findById(any(Long.class))).thenReturn(Optional.of(group));
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        assertThat(groupService.findById(group.groupId())).contains(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when updated group successfully should return true")
    void whenUpdatedGroupSuccessfullyShouldReturnTrue(Group group, GroupDto groupDto) {
        when(groupDao.update(any(Group.class), any(Long.class))).thenReturn(true);
        when(groupMapper.dtoToModel(any(GroupDto.class))).thenReturn(group);

        assertThat(groupService.update(groupDto, group.groupId())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when successfully deleting group should return true")
    void whenSuccessfullyDeletingGroupShouldReturnTrue(Group group) {
        when(groupDao.delete(any(Long.class))).thenReturn(true);

        assertThat(groupService.delete(group.groupId())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when finding group by name should return the right group")
    void whenFindingGroupByNameShouldReturnTheRightGroup(Group group, GroupDto groupDto) {

        when(groupDao.findByName(any(String.class))).thenReturn(Optional.of(group));
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        assertThat(groupService.findByName(group.groupName())).contains(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupDtoProvider")
    @DisplayName("when finding all groups by max student count should return right groups")
    void whenFindingAllGroupsByMaxStudentCountShouldReturnRightGroups(List<Group> groups, List<GroupDto> expected) {
        when(groupDao.findAllByMaxStudentCount(any(Integer.class), any(Pageable.class))).thenReturn(new SliceImpl<>(
                groups));
        when(groupMapper.modelsToDtos(any(Iterable.class))).thenReturn(new SliceImpl<>(expected));

        final Slice<GroupDto> actual = groupService.findAllByMaxStudentCount(1, mock(Pageable.class));

        assertThat(actual).isEqualTo(new SliceImpl<>(expected));
    }
}