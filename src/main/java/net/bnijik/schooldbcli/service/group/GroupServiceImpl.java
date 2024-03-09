package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import net.bnijik.schooldbcli.repository.GroupRepository;
import net.bnijik.schooldbcli.service.schoolAdmin.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupServiceImpl extends SchoolAdminServiceImpl<GroupDto, Group> implements GroupService {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper, GroupRepository groupRepository) {
        super(groupMapper, groupRepository);
        this.groupMapper = groupMapper;
        this.groupRepository = groupRepository;
    }

    @Override
    public Optional<GroupDto> findByName(String groupName) {
        final Optional<Group> groupOptional = groupRepository.findByGroupName(groupName);
        return groupOptional.map(groupMapper::modelToDto);
    }

    @Override
    public Slice<GroupDto> findAllByMaxStudentCount(int maxStudentCount, Pageable pageable) {
        final Slice<Group> groups = groupRepository.findAllByMaxStudentCount(maxStudentCount, pageable);
        return groupMapper.modelsToDtos(groups);
    }
}
