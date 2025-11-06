package com.posicube.assignment.querylog.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaQueryLogRepository extends JpaRepository<QueryLogJpaEntity, Long> {
}
