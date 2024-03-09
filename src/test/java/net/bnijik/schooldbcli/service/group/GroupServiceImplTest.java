package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import net.bnijik.schooldbcli.repository.GroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;

    @Mock
    GroupMapper groupMapper;

    @InjectMocks
    GroupServiceImpl groupService;


    private static Stream<Arguments> groupProvider() {
        return Stream.of(Arguments.of(new Group(new ArrayList<>(), 23L, "Some cool group"),
                                      new GroupDto(23L, "Some cool group")));
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
    @DisplayName("when successfully creating new group should return new group")
    void whenSuccessfullyCreatingNewGroupShouldReturnNewGroup(Group group, GroupDto groupDto) {
        when(groupRepository.persist(any(Group.class))).thenReturn(group);
        when(groupMapper.dtoToModel(any(GroupDto.class))).thenReturn(group);
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        final GroupDto created = groupService.create(groupDto);

        assertThat(created).isEqualTo(groupDto);
        assertThat(created).isSameAs(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupDtoProvider")
    @DisplayName("when finding all groups should return all groups")
    void whenFindingAllGroupsShouldReturnAllGroups(List<Group> groups, List<GroupDto> expected) {
        when(groupRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(groups));
        final PageImpl<GroupDto> expectedPageable = new PageImpl<>(expected);
        when(groupMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<GroupDto> actual = groupService.findAll(mock(Pageable.class));

        assertThat(actual).isEqualTo(expectedPageable);
        assertThat(actual).isSameAs(expectedPageable);
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when finding group by id should return the correct group")
    void whenFindingGroupByIdShouldReturnTheCorrectGroup(Group group, GroupDto groupDto) {

        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.of(group));
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        assertThat(groupService.findById(group.groupId())).contains(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when updated group successfully should return updated group")
    void whenUpdatedGroupSuccessfullyShouldReturnUpdatedGroup(Group group, GroupDto groupDto) {
        when(groupRepository.update(any(Group.class))).thenReturn(group);
        when(groupMapper.dtoToModel(any(GroupDto.class))).thenReturn(group);
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        final GroupDto updated = groupService.update(groupDto);

        assertThat(updated).isEqualTo(groupDto);
        assertThat(updated).isSameAs(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when deleting group should use repo delete by id method")
    void whenDeletingGroupShouldUseRepoDeleteByIdMethod(Group group) {
        groupService.delete(group.groupId());
        verify(groupRepository, times(1)).deleteById(group.groupId());
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    @DisplayName("when finding group by name should return the right group")
    void whenFindingGroupByNameShouldReturnTheRightGroup(Group group, GroupDto groupDto) {

        when(groupRepository.findByGroupName(any(String.class))).thenReturn(Optional.of(group));
        when(groupMapper.modelToDto(any(Group.class))).thenReturn(groupDto);

        assertThat(groupService.findByName(group.groupName())).contains(groupDto);
    }

    @ParameterizedTest
    @MethodSource("groupDtoProvider")
    @DisplayName("when finding all groups by max student count should return right groups")
    void whenFindingAllGroupsByMaxStudentCountShouldReturnRightGroups(List<Group> groups, List<GroupDto> expected) {
        when(groupRepository.findAllByMaxStudentCount(any(Integer.class),
                                                      any(Pageable.class))).thenReturn(new PageImpl<>(
                groups));
        final SliceImpl<GroupDto> expectedPageable = new SliceImpl<>(expected);
        when(groupMapper.modelsToDtos(any(Iterable.class))).thenReturn(expectedPageable);

        final Slice<GroupDto> actual = groupService.findAllByMaxStudentCount(1, mock(Pageable.class));

        assertThat(actual).isEqualTo(expectedPageable);
        assertThat(actual).isSameAs(expectedPageable);
    }
}