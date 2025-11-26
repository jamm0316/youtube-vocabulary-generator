package com.personalproject.llmmanager.querylog.adapter.in.web.response;

import com.personalproject.llmmanager.querylog.application.dtos.result.QueryResult;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.users.domain.model.Users;

public record QueryResponse (
        String userName,
        String answer,
        String model,
        Long usedToken,
        Long remainingToken
) {

    public static QueryResponse from(QueryResult queryResult) {
        return new QueryResponse(
                queryResult.userName(),
                queryResult.answer(),
                queryResult.model(),
                queryResult.usedToken(),
                queryResult.remainingToken()
        );
    }
}
