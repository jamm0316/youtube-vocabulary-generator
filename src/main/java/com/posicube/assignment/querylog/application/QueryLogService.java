package com.posicube.assignment.querylog.application;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.common.utils.TokenCalculator;
import com.posicube.assignment.querylog.domain.ModelType;
import com.posicube.assignment.querylog.domain.port.QueryLogRepository;
import com.posicube.assignment.querylog.domain.vo.QueryLog;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.querylog.presentation.dtos.QueryRequest;
import com.posicube.assignment.querylog.presentation.dtos.QueryResponse;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLogService {
    private final UserRepository userRepository;
    private final RateLimiter rateLimiter;
    private final LlmClient llmClient;
    private final TokenCalculator tokenCalculator;
    private final QueryLogRepository queryLogRepository;

    @Transactional
    public QueryResponse submitQuery(Long userId, QueryRequest request) {
        //1. 사용자 조회
        Users user = userRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        //2. Rate Limit 검증
        if (!rateLimiter.isAllowed(userId)) {
            throw new BaseException(QueryLogExceptionStatus.TOO_MANY_REQUESTS);
        }

        //3. 잔여 토큰 확인
        user.validateQueryPermission();

        //4. llm 호출
        long usedTokens = tokenCalculator.calculateTokensFromPrompt(request.q());
        String answer = llmClient.query(request.q(), request.model());

        //토큰 사용
        user.useTokens(usedTokens);

        //6. queryLog 저장
        QueryLog queryLog = QueryLog.create(user, request.q(), ModelType.from(request.model()), usedTokens);
        queryLogRepository.save(queryLog);

        return QueryResponse.from(answer);
    }
}
