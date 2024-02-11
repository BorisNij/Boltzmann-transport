package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcStudentDao implements StudentDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcStudentDao.class);
    private final SimpleJdbcInsert insert;
    private final NamedParameterJdbcTemplate template;
    private final StudentQueries queries;
    private final RowMapper<Student> rowMapper = (rowSet, rowNum) -> new Student(rowSet.getLong(StudentQueries.STUDENT_ID_COLUMN),
                                                                                 rowSet.getLong(StudentQueries.STUDENT_GROUP_ID_COLUMN),
                                                                                 rowSet.getString(StudentQueries.STUDENT_FIRST_NAME_COLUMN),
                                                                                 rowSet.getString(StudentQueries.STUDENT_LAST_NAME_COLUMN));

    @Autowired
    public JdbcStudentDao(NamedParameterJdbcTemplate template, SimpleJdbcInsert insert, StudentQueries queries) {
        this.insert = insert.withTableName(StudentQueries.STUDENT_TABLE_NAME)
                .usingGeneratedKeyColumns(StudentQueries.STUDENT_ID_COLUMN);
        this.template = template;
        this.queries = queries;
    }

    @Override
    public List<Student> findAll(Page page) {
        String sql = queries.finaAll();
        try {
            log.debug("Finding all students with limit {}, offset {}", page.getLimit(), page.getOffset());
            return template.query(sql,
                                  Map.of(Page.PAGE_LIMIT_PARAM,
                                         page.getLimit(),
                                         Page.PAGE_OFFSET_PARAM,
                                         page.getOffset()),
                                  rowMapper);
        } catch (DataAccessException e) {
            log.error("Error finding all students: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public long save(Student student) {
        try {
            log.debug("Saving {}", student);
            final Number id = insert.executeAndReturnKey(Map.of(StudentQueries.STUDENT_GROUP_ID_COLUMN,
                                                                student.groupId(),
                                                                StudentQueries.STUDENT_FIRST_NAME_COLUMN,
                                                                student.firstName(),
                                                                StudentQueries.STUDENT_LAST_NAME_COLUMN,
                                                                student.lastName()));
            if (id.longValue() == 0) {
                log.error("Saving student failed. Returned ID: {}", id);
                return 0;
            }
            log.info("Successfully saved new student with ID {}", id);
            return id.longValue();
        } catch (DataAccessException e) {
            log.error("Error saving {}: {}", student, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Student> findById(long id) {
        String sql = queries.findById();
        try {
            log.debug("Finding student with ID {}", id);
            return DataAccessUtils.optionalResult(template.query(sql,
                                                                 Map.of(StudentQueries.STUDENT_ID_PARAM, id),
                                                                 rowMapper));
        } catch (DataAccessException e) {
            log.error("Error finding student with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Student newStudent, long existingStudentId) {
        String sql = queries.update();
        try {
            log.debug("Updating existing student having ID {} with new details {}", existingStudentId, newStudent);
            final int rowsAffected = template.update(sql,
                                                     Map.of(StudentQueries.STUDENT_FIRST_NAME_PARAM,
                                                            newStudent.firstName(),
                                                            StudentQueries.STUDENT_LAST_NAME_PARAM,
                                                            newStudent.lastName(),
                                                            StudentQueries.STUDENT_GROUP_ID_PARAM,
                                                            newStudent.groupId(),
                                                            StudentQueries.STUDENT_ID_PARAM,
                                                            existingStudentId));
            if (rowsAffected == 0) {
                log.warn("No rows were affected while attempting to update existing student having ID {}",
                         existingStudentId);
                return false;
            }
            log.info("Successfully updated student having ID {} with new details {}", existingStudentId, newStudent);
            return true;
        } catch (DataAccessException e) {
            log.error("Error updating existing student with ID {}: {}", existingStudentId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = queries.deleteById();
        try {
            log.debug("Deleting student with ID {}", id);
            final int rowsAffected = template.update(sql, Map.of(StudentQueries.STUDENT_ID_PARAM, id));
            if (rowsAffected == 0) {
                log.warn("No rows were affected while deleting student with {}", id);
                return false;
            }
            log.info("Successfully delete student with ID {}", id);
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting student with ID {}: {}", id, e.getMessage(), e);
            throw e;

        }
    }

    @Override
    public List<Student> findAllByCourseName(String courseName, Page page) {
        String sql = queries.findAllByCourseName();
        try {
            log.debug("Finding all students enrolled in '{}' course with limit {} and offset {}",
                      courseName,
                      page.getLimit(),
                      page.getOffset());
            return template.query(sql,
                                  Map.of(StudentQueries.STUDENT_COURSE_NAME_PARAM,
                                         courseName,
                                         Page.PAGE_LIMIT_PARAM,
                                         page.getLimit(),
                                         Page.PAGE_OFFSET_PARAM,
                                         page.getOffset()),
                                  rowMapper);
        } catch (DataAccessException e) {
            log.error("Error retrieving all students enrolled in '{}' course: {}", courseName, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public boolean enrollInCourses(long studentId, List<Long> courseIds) {
        String sql = queries.enrollInCourses();
        try {
            log.debug("Enrolling student with ID {} into courses with IDs {}", studentId, courseIds);
            final MapSqlParameterSource[] paramSources = courseIds.stream()
                    .map(courseId -> new MapSqlParameterSource().addValue("studentId", studentId)
                            .addValue("courseId", courseId))
                    .toArray(MapSqlParameterSource[]::new);
            int[] rowsInserted = template.batchUpdate(sql, paramSources);

            for (int rows : rowsInserted) {
                if (rows <= 0) {
                    log.error("Failed to enroll student with ID {} in courses with IDs {}", studentId, courseIds);
                    return false;
                }
            }
            log.info("Successfully enrolled student with ID {} in courses with IDs {}", studentId, courseIds);
            return true;
        } catch (DataAccessException e) {
            log.error("Error enrolling student with ID {} in courses with IDs {}: {}",
                      studentId,
                      courseIds,
                      e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean withdrawFromCourse(long studentId, long courseId) {
        String sql = queries.withdrawFromCourse();
        try {
            log.debug("Withdrawing student with ID {} from course with ID {}", studentId, courseId);
            final int rowsAffected = template.update(sql,
                                                     Map.of(StudentQueries.STUDENT_ID_PARAM,
                                                            studentId,
                                                            StudentQueries.STUDENT_COURSE_ID_PARAM,
                                                            courseId));
            if (rowsAffected == 0) {
                log.warn("No rows were affected while withdrawing student with ID {} from course with ID {}",
                         studentId,
                         courseId);
                return false;
            }
            log.info("Successfully withdrew student with ID {} from course with ID {}", studentId, courseId);
            return true;
        } catch (DataAccessException e) {
            log.error("Error withdrawing student with ID {} from course with ID {}: {}",
                      studentId,
                      courseId,
                      e.getMessage());
            throw e;
        }
    }
}
