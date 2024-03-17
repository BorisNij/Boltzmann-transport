package net.bnijik.schooldbcli.repository;

import jakarta.transaction.Transactional;
import net.bnijik.schooldbcli.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends SchoolRepository<Student> {
    Slice<Student> findAllByCoursesCourseName(String courseName, Pageable pageable);

    @Transactional
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.studentId = :studentId")
    Optional<Student> findByIdWithCourses(long studentId);

}
