package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.entity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcGroupDao.class);
    private final SimpleJdbcInsert insert;
    private final JdbcClient jdbcClient;
    private final GroupQueries queries;
    private final RowMapper<Group> rowMapper = (rowSet, rowNum) -> new Group(rowSet.getLong(GroupQueries.GROUP_ID_COLUMN),
                                                                             rowSet.getString(GroupQueries.GROUP_NAME_COLUMN));

    @Autowired
    public JdbcGroupDao(JdbcClient jdbcClient, SimpleJdbcInsert insert, GroupQueries queries) {
        this.insert = insert.withTableName(GroupQueries.GROUP_TABLE_NAME)
                .usingGeneratedKeyColumns(GroupQueries.GROUP_ID_COLUMN);
        this.jdbcClient = jdbcClient;
        this.queries = queries;
    }

    @Override
    public long save(Group group) {
        try {
            log.debug("Saving {}", group);
            final Number id = insert.executeAndReturnKey(Map.of(GroupQueries.GROUP_NAME_COLUMN, group.groupName()));
            if (id.longValue() == 0) {
                log.error("Saving {}. Returned ID: {}", group, id);
                return 0;
            }
            log.info("Successfully saved new group {} with ID {}", group, id);
            return id.longValue();
        } catch (DataAccessException e) {
            log.error("Error saving {}: {}", group, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Group> findById(long id) {
        try {
            log.debug("Finding group with ID {}", id);
            return jdbcClient.sql(queries.findById())
                    .params(Map.of(GroupQueries.GROUP_ID_PARAM, id))
                    .query(rowMapper)
                    .optional();
        } catch (DataAccessException e) {
            log.error("Error finding group with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Group> findAll(Page page) {
        try {
            log.debug("Finding all groups with limit {}, offset {}", page.getLimit(), page.getOffset());
            return jdbcClient.sql(queries.finaAll())
                    .params(Map.of(Page.PAGE_LIMIT_PARAM, page.getLimit(), Page.PAGE_OFFSET_PARAM, page.getOffset()))
                    .query(rowMapper)
                    .list();
        } catch (DataAccessException e) {
            log.error("Error finding all groups with limit {}, offset {}: {}",
                      page.getLimit(),
                      page.getOffset(),
                      e.getMessage(),
                      e);
            throw e;
        }
    }

    @Override
    public Optional<Group> findByName(String name) {
        try {
            log.debug("Finding group by name {}", name);
            return jdbcClient.sql(queries.findByName())
                    .params(Map.of(GroupQueries.GROUP_NAME_PARAM, name))
                    .query(rowMapper)
                    .optional();
        } catch (DataAccessException e) {
            log.error("Error finding group with name {}: {}", name, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Group> findAllByMaxStudentCount(int maxStudentCount, Page page) {
        try {
            log.debug("Finding all groups having at most {} students with limit {} and offset {}",
                      maxStudentCount,
                      page.getLimit(),
                      page.getOffset());
            return jdbcClient.sql(queries.findAllByMaxStudCount())
                    .params(Map.of(GroupQueries.GROUP_STUD_COUNT_PARAM,
                                   maxStudentCount,
                                   Page.PAGE_LIMIT_PARAM,
                                   page.getLimit(),
                                   Page.PAGE_OFFSET_PARAM,
                                   page.getOffset()))
                    .query(rowMapper)
                    .list();
        } catch (DataAccessException e) {
            log.error("Error finding all groups having at most {} students with limit {} and offset {}: {}",
                      page.getLimit(),
                      page.getOffset(),
                      maxStudentCount,
                      e.getMessage(),
                      e);
            throw e;
        }
    }

    @Override
    public boolean update(Group newGroup, long existingGroupId) {
        try {
            log.debug("Updating group having ID {} with new name {}", existingGroupId, newGroup.groupName());
            final int rowsAffected = jdbcClient.sql(queries.update())
                    .params(Map.of(GroupQueries.GROUP_NAME_PARAM,
                                   newGroup.groupName(),
                                   GroupQueries.GROUP_ID_PARAM,
                                   existingGroupId))
                    .update();
            if (rowsAffected == 0) {
                log.warn("No rows were affected while attempting to update group with ID {}", existingGroupId);
                return false;
            }
            log.info("Successfully updated group having ID {} with new name {}", existingGroupId, newGroup.groupName());
            return true;
        } catch (DataAccessException e) {
            log.error("Error updating group with ID {}: {}", existingGroupId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            log.debug("Deleting group with ID {}", id);
            final int rowsAffected = jdbcClient.sql(queries.deleteById())
                    .params(Map.of(GroupQueries.GROUP_ID_PARAM, id))
                    .update();
            if (rowsAffected == 0) {
                log.warn("No rows were affected while deleting group with {}", id);
                return false;
            }
            log.info("Successfully delete group with ID {}", id);
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting group with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
