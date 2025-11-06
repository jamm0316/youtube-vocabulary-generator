package com.posicube.assignment.querylog.application.commandquery;

import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.users.domain.model.Users;

public record QueryResponse (
        String userName,
        String answer,
        String model,
        Long usedToken,
        Long remainingToken
) {
    /**
     * 도메인 객체들로 부터 QueryResponse DTO를 생성합니다.
     * @
     */
    public static QueryResponse of (QueryLog queryLog, Users users) {
        return new QueryResponse(
                users.getName(),
                queryLog.getAnswer(),
                queryLog.getType().getName(),
                queryLog.getUsedTokens(),
                users.getTokens().getRemainingTokens()
        );
    }
}
