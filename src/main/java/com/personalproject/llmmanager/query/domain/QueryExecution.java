package com.personalproject.llmmanager.query.domain;

import com.personalproject.llmmanager.querylog.application.dtos.result.QueryResult;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.users.domain.model.Users;

/**
 * VO: Query 실행 결과
 */
public record QueryExecution (Users users, QueryLog queryLog) {
    public QueryResult toResult() {
        return QueryResult.of(queryLog, users);
    }
}
