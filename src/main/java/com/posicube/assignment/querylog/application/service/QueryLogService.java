package com.posicube.assignment.querylog.application.service;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.application.commandquery.QueryRequest;
import com.posicube.assignment.querylog.application.commandquery.QueryResponse;
import com.posicube.assignment.querylog.domain.model.ModelType;
import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.querylog.domain.policy.TokenCalculator;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.querylog.port.out.QueryLogRepository;
import com.posicube.assignment.users.domain.model.Users;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import com.posicube.assignment.users.port.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLogService {
    private final QueryLogRepository queryLogRepository;
    private final UsersRepository usersRepository;
    private final RateLimiter rateLimiter;
    private final LlmClient llmClient;
    private final TokenCalculator tokenCalculator;

    @Transactional
    public QueryResponse submitQuery(Long userId, QueryRequest request) {
        //1. 사용자 조회
        Users user = usersRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        //2. Rate Limit 검증
        if (!rateLimiter.isAllowed(userId)) {
            throw new BaseException(QueryLogExceptionStatus.TOO_MANY_REQUESTS);
        }

        //3. 잔여 토큰 확인
        user.validateQueryPermission();

        //4. llm 호출
        Long usedTokens = tokenCalculator.calculateTokensFromPrompt(request.q());
        String answer;

        try {
            answer = llmClient.query(request.q(), request.model());
        } catch (Exception e) {
            throw new BaseException(QueryLogExceptionStatus.LLM_API_ERROR);
        }

        //5. 토큰 사용
        Users userWithTokensUsed = user.useTokens(usedTokens);

        //6. 변경된 도메인 객체를 Repository에 전달하여 저장(더티체킹x)
        Users updatedUser = usersRepository.save(userWithTokensUsed);

        //6. queryLog 저장
        QueryLog queryLog = QueryLog.create(user, request.q(), ModelType.from(request.model()), answer, usedTokens);
        QueryLog saveQuery = queryLogRepository.save(queryLog);

        return QueryResponse.of(saveQuery, updatedUser);
    }
}
