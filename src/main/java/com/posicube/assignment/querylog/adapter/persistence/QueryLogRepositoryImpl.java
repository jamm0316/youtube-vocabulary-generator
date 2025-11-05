package com.posicube.assignment.querylog.adapter.persistence;

import com.posicube.assignment.querylog.domain.port.QueryLogRepository;
import com.posicube.assignment.querylog.domain.vo.QueryLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryLogRepositoryImpl implements QueryLogRepository {
    private final SpringDataJpaQueryLogRepository jpa;

    @Override
    public QueryLog save(QueryLog queryLog) {
        return jpa.save(queryLog);
    }
}
