package net.bnijik.schooldbcli.repository;

import net.bnijik.schooldbcli.repository.hibernate.HibernateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface SchoolRepository<M> extends JpaRepository<M, Long>, HibernateRepository<M> {
}
