package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dao.group.GroupDao;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.mapper.GroupMapper;
import net.bnijik.schooldbcli.service.SchoolAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupServiceImpl extends SchoolAdminServiceImpl<GroupDto, Group> implements GroupService {

    private final GroupMapper groupMapper;
    private final GroupDao groupDao;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper, GroupDao groupDao) {
        super(groupMapper, groupDao);
        this.groupMapper = groupMapper;
        this.groupDao = groupDao;
    }

    @Override
    public Optional<GroupDto> findByName(String groupName) {
        final Optional<Group> groupOptional = groupDao.findByName(groupName);
        return groupOptional.map(groupMapper::modelToDto);
    }

    @Override
    public Slice<GroupDto> findAllByMaxStudentCount(int maxStudentCount, Pageable pageable) {
        final Slice<Group> groups = groupDao.findAllByMaxStudentCount(maxStudentCount, pageable);
        return groupMapper.modelsToDtos(groups);
    }
}
