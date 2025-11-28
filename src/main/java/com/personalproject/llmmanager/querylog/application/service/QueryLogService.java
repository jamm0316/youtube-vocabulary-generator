package com.personalproject.llmmanager.querylog.application.service;

import com.personalproject.llmmanager.common.baseResponse.BaseResponse;
import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.query.domain.Query;
import com.personalproject.llmmanager.query.domain.QueryExecution;
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
        Users user = findUser(userId);

        //2. Query 도메인 생성 및 토큰 계산
        Query query = Query.start(user, command.url(), ModelType.from(command.model())).calculateTokens(tokenCalculator);

        //3. llm 호출
        String answer = callLlm(command);

        //4. Query 완료 및 실행
        QueryExecution execution = query.complete(answer).execute();

        //5. 영속화
        usersRepository.save(execution.users());
        queryLogRepository.save(execution.queryLog());

        return execution.toResult();
    }

    private Users findUser(Long userId) {
        return usersRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));
    }

    private String callLlm(QueryCommand command) {
        try {
            //todo: url v=~~~~ -> video id만 param으로 넘긴다.
            //todo: param token -> access Token
            return llmPort.query(command.url(), command.model());
        } catch (Exception e) {
            throw new BaseException(QueryLogExceptionStatus.LLM_API_ERROR);
        }
    }
}
