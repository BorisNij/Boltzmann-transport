package net.bnijik.schooldbcli.service.group;

import net.bnijik.schooldbcli.dto.GroupDto;
import net.bnijik.schooldbcli.service.schoolAdmin.SchoolAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface GroupService extends SchoolAdminService<GroupDto> {
    Optional<GroupDto> findByName(String groupName);

    Slice<GroupDto> findAllByMaxStudentCount(int maxStudentCount, Pageable pageable);
}
