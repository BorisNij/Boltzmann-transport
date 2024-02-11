package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends Dao<Group> {
    Optional<Group> findByName(String groupName);

    List<Group> findAllByMaxStudentCount(int maxStudentCount, Page page);
}
