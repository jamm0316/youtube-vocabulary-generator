package com.llmmanager.assignment.querylog.application.service;

import com.llmmanager.assignment.common.exception.BaseException;
import com.llmmanager.assignment.querylog.application.commandquery.QueryRequest;
import com.llmmanager.assignment.querylog.application.commandquery.QueryResponse;
import com.llmmanager.assignment.querylog.domain.model.ModelType;
import com.llmmanager.assignment.querylog.domain.model.QueryLog;
import com.llmmanager.assignment.querylog.domain.policy.TokenCalculator;
import com.llmmanager.assignment.querylog.exception.QueryLogExceptionStatus;
import com.llmmanager.assignment.querylog.port.LlmPort;
import com.llmmanager.assignment.querylog.port.QueryLogRepository;
import com.llmmanager.assignment.users.domain.model.Users;
import com.llmmanager.assignment.users.exception.UserExceptionStatus;
import com.llmmanager.assignment.users.port.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLogService {
    private final QueryLogRepository queryLogRepository;
    private final UsersRepository usersRepository;
    private final LlmPort llmPort;
    private final TokenCalculator tokenCalculator;

    @Transactional
    public QueryResponse submitQuery(Long userId, QueryRequest request) {
        //1. 사용자 조회
        Users user = usersRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        //2. 잔여 토큰 확인
        user.validateQueryPermission();

        //3. llm 호출
        Long usedTokens = tokenCalculator.calculateTokensFromPrompt(request.q());
        String answer;

        try {
            answer = llmPort.query(request.q(), request.model());
        } catch (Exception e) {
            throw new BaseException(QueryLogExceptionStatus.LLM_API_ERROR);
        }

        //4. 토큰 사용
        Users userWithTokensUsed = user.useTokens(usedTokens);

        //5. 변경된 사용자 정보를 Repository에 전달하여 저장
        Users updatedUser = usersRepository.save(userWithTokensUsed);

        //6. queryLog 저장
        QueryLog queryLog = QueryLog.create(updatedUser, request.q(), ModelType.from(request.model()), answer, usedTokens);
        QueryLog saveQuery = queryLogRepository.save(queryLog);

        return QueryResponse.of(saveQuery, updatedUser);
    }
}
