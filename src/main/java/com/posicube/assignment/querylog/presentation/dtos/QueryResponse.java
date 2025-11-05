package com.posicube.assignment.querylog.presentation.dtos;

import com.posicube.assignment.querylog.domain.QueryLog;

public record QueryResponse (
        String userName,
        String answer,
        String model,
        Long usedToken,
        Long remainingToken
) {
    public static QueryResponse from(QueryLog queryLog) {
        return new QueryResponse(
                queryLog.getUser().getName(),
                queryLog.getAnswer(),
                queryLog.getType().getName(),
                queryLog.getUsedTokens(),
                queryLog.getUser().getTokens().getRemainingTokens());
    }
}
