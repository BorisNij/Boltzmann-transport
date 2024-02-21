package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.entity.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface GroupDao extends Dao<Group> {
    Optional<Group> findByName(String groupName);

    Slice<Group> findAllByMaxStudentCount(int maxStudentCount, Pageable page);
}
