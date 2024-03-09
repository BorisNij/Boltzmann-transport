package net.bnijik.schooldbcli.repository;

import net.bnijik.schooldbcli.entity.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends SchoolRepository<Group> {
    Optional<Group> findByGroupName(String groupName);

    @Query("SELECT g FROM Group g WHERE SIZE(g.students) <= :maxStudentCount")
    Slice<Group> findAllByMaxStudentCount(@Param("maxStudentCount") int maxStudentCount, Pageable pageable);
}
