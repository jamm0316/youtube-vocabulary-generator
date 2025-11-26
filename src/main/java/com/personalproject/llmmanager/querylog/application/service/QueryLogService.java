package com.personalproject.llmmanager.querylog.application.service;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.querylog.adapter.in.web.request.QueryRequest;
import com.personalproject.llmmanager.querylog.adapter.in.web.response.QueryResponse;
import com.personalproject.llmmanager.querylog.application.dtos.command.QueryCommand;
import com.personalproject.llmmanager.querylog.application.dtos.result.QueryResult;
import com.personalproject.llmmanager.querylog.domain.model.ModelType;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.querylog.domain.policy.TokenCalculator;
import com.personalproject.llmmanager.querylog.exception.QueryLogExceptionStatus;
import com.personalproject.llmmanager.querylog.port.LlmPort;
import com.personalproject.llmmanager.querylog.port.QueryLogRepository;
import com.personalproject.llmmanager.users.domain.model.Users;
import com.personalproject.llmmanager.users.exception.UserExceptionStatus;
import com.personalproject.llmmanager.users.port.UsersRepository;
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
    public QueryResult submitQuery(Long userId, QueryCommand command) {
        //1. 사용자 조회
        Users user = usersRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        //2. 잔여 토큰 확인
        user.validateQueryPermission();

        Long usedTokens = tokenCalculator.calculateTokensFromPrompt(command.url());
        String answer;

        //3. llm 호출
        try {
            //todo: url v=~~~~ -> video id만 param으로 넘긴다.
            //todo: param token -> access Token
            answer = llmPort.query(command.url(), command.model());
        } catch (Exception e) {
            throw new BaseException(QueryLogExceptionStatus.LLM_API_ERROR);
        }

        //4. 토큰 사용
        Users userWithTokensUsed = user.useTokens(usedTokens);

        //5. 변경된 사용자 정보를 Repository에 전달하여 저장
        Users updatedUser = usersRepository.save(userWithTokensUsed);

        //6. queryLog 저장
        QueryLog queryLog = QueryLog.create(updatedUser, command.url(), ModelType.from(command.model()), answer, usedTokens);
        QueryLog saveQuery = queryLogRepository.save(queryLog);

        return QueryResult.of(saveQuery, updatedUser);
    }
}
