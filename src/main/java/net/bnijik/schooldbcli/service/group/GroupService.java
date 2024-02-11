package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.service.SchoolAdminService;

import java.util.List;
import java.util.Optional;

public interface GroupService extends SchoolAdminService<GroupDto> {
    Optional<GroupDto> findByName(String groupName);

    List<GroupDto> findAllByMaxStudentCount(int maxStudentCount, Page page);
}
