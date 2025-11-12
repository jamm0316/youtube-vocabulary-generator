package com.personalproject.llmmanager.querylog.application.commandquery;

import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.users.domain.model.Users;

public record QueryResponse (
        String userName,
        String answer,
        String model,
        Long usedToken,
        Long remainingToken
) {
    /**
     * 도메인 객체들로 부터 QueryResponse DTO를 생성합니다.
     * @param queryLog 답변을 담은 QueryLog 정보
     * @param users 요청을 보낸 Users 정보
     * @return 쿼리 요청에 대한 응답 객체
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
