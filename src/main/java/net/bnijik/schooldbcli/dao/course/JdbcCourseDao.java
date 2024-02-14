package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.entity.Course;
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
public class JdbcCourseDao implements CourseDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcCourseDao.class);
    private final SimpleJdbcInsert insert;
    private final JdbcClient jdbcClient;
    private final CourseQueries queries;
    private final RowMapper<Course> rowMapper = (rowSet, rowNum) -> new Course(rowSet.getLong(CourseQueries.COURSE_ID_COLUMN),
                                                                               rowSet.getString(CourseQueries.COURSE_NAME_COLUMN),
                                                                               rowSet.getString(CourseQueries.COURSE_DESCRIPTION_COLUMN));

    @Autowired
    public JdbcCourseDao(SimpleJdbcInsert insert, JdbcClient jdbcClient, CourseQueries queries) {
        this.insert = insert.withTableName(CourseQueries.COURSE_TABLE_NAME)
                .usingGeneratedKeyColumns(CourseQueries.COURSE_ID_COLUMN);
        this.jdbcClient = jdbcClient;
        this.queries = queries;
    }


    @Override
    public long save(Course course) {
        try {
            log.debug("Saving course '{}'", course.courseName());
            final Number id = insert.executeAndReturnKey(Map.of(CourseQueries.COURSE_NAME_COLUMN,
                                                                course.courseName(),
                                                                CourseQueries.COURSE_DESCRIPTION_COLUMN,
                                                                course.courseDescription()));
            if (id.longValue() == 0) {
                log.error("Saving course failed. Returned ID: {}", id);
                return 0;
            }
            return id.longValue();
        } catch (DataAccessException e) {
            log.error("Error saving {}: {}", course, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Course> findAll(Page page) {
        try {
            log.debug("Finding all courses with limit {} and offset {}", page.getLimit(), page.getOffset());
            return jdbcClient.sql(queries.finaAll())
                    .params(Map.of(Page.PAGE_LIMIT_PARAM, page.getLimit(), Page.PAGE_OFFSET_PARAM, page.getOffset()))
                    .query(rowMapper)
                    .list();
        } catch (DataAccessException e) {
            log.error("Error retrieving all courses: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Course> findAllForStudent(long studentId, Page page) {
        try {
            log.debug("Finding all courses for student having ID {} with limit {} and offset {}",
                      studentId,
                      page.getLimit(),
                      page.getOffset());
            return jdbcClient.sql(queries.findAllForStudent())
                    .params(Map.of(CourseQueries.STUDENT_ID_PARAM,
                                   studentId,
                                   Page.PAGE_LIMIT_PARAM,
                                   page.getLimit(),
                                   Page.PAGE_OFFSET_PARAM,
                                   page.getOffset()))
                    .query(rowMapper)
                    .list();
        } catch (DataAccessException e) {
            log.error("Error retrieving all courses having for student having ID {}: {}", studentId, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public Optional<Course> findById(long id) {
        try {
            log.debug("Finding course with ID {}", id);
            return jdbcClient.sql(queries.findById())
                    .params(Map.of(CourseQueries.COURSE_ID_PARAM, id))
                    .query(rowMapper)
                    .optional();
        } catch (DataAccessException e) {
            log.error("Error finding course with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Course newCourse, long existingCourseId) {
        try {
            log.debug("Updating course having ID {} with new name '{}'", existingCourseId, newCourse.courseName());
            final int rowsAffected = jdbcClient.sql(queries.update())
                    .params(Map.of(CourseQueries.COURSE_NAME_PARAM,
                                   newCourse.courseName(),
                                   CourseQueries.COURSE_DESCRIPTION_PARAM,
                                   newCourse.courseDescription(),
                                   CourseQueries.COURSE_ID_PARAM,
                                   existingCourseId))
                    .update();
            if (rowsAffected == 0) {
                log.error("Failed to update course with ID {}. No rows were affected.", existingCourseId);
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error("Error updating course with ID {}: {}", existingCourseId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            log.debug("Deleting course with ID {}", id);
            final int rowsAffected = jdbcClient.sql(queries.deleteById())
                    .params(Map.of(CourseQueries.COURSE_ID_PARAM, id))
                    .update();
            if (rowsAffected == 0) {
                log.error("Failed to delete course with ID {}. No rows were affected.", id);
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting course with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
