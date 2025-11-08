package com.posicube.assignment.querylog.port;

import com.posicube.assignment.querylog.domain.model.QueryLog;

import java.util.List;

public interface QueryLogRepository {
    QueryLog save(QueryLog queryLog);
    List<QueryLog> findAllByUserId(Long userId);
}
