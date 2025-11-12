package com.personalproject.llmmanager.querylog.port;

import com.personalproject.llmmanager.querylog.domain.model.QueryLog;

import java.util.List;

public interface QueryLogRepository {
    QueryLog save(QueryLog queryLog);
    List<QueryLog> findAllByUserId(Long userId);
}
