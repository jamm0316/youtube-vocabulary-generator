package com.posicube.assignment.querylog.domain.port;

import com.posicube.assignment.querylog.domain.QueryLog;

public interface QueryLogRepository {
    QueryLog save(QueryLog queryLog);
}
