package com.llmmanager.assignment.querylog.adapter.out.persistence;

import com.llmmanager.assignment.users.adapter.out.persistence.UsersJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaQueryLogRepository extends JpaRepository<QueryLogJpaEntity, Long> {
    List<QueryLogJpaEntity> findAllByUser(UsersJpaEntity user);

    UsersJpaEntity user(UsersJpaEntity user);
}
