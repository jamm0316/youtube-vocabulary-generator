package com.llmmanager.assignment.querylog.port;

import com.llmmanager.assignment.querylog.domain.model.QueryLog;

import java.util.List;

public interface QueryLogRepository {
    QueryLog save(QueryLog queryLog);
    List<QueryLog> findAllByUserId(Long userId);
}
