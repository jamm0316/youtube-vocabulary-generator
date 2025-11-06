package com.posicube.assignment.querylog.port.out;

import com.posicube.assignment.querylog.domain.model.QueryLog;

public interface QueryLogRepository {
    QueryLog save(QueryLog queryLog);
}
