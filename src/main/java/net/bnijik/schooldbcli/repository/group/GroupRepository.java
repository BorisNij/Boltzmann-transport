package net.bnijik.schooldbcli.repository.group;

import net.bnijik.schooldbcli.entity.Group;
import net.bnijik.schooldbcli.repository.HibernateRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, HibernateRepository<Group> {
    Optional<Group> findByGroupName(String groupName);

    @Query("SELECT g FROM Group g WHERE SIZE(g.students) <= :maxStudentCount")
    Slice<Group> findAllByMaxStudentCount(@Param("maxStudentCount") int maxStudentCount, Pageable pageable);
}
