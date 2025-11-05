package com.posicube.assignment.querylog.adapter.persistence;

import com.posicube.assignment.querylog.domain.vo.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaQueryLogRepository extends JpaRepository<QueryLog, Long> {
}
