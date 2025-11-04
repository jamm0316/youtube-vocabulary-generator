package com.posicube.assignment.querylog.application;

import com.posicube.assignment.common.exception.BaseException;
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
public class QueryService {
    private final UserRepository userRepository;
    private final RateLimiter rateLimiter;

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

        //llm 호출

        //토큰 사용

        return null;
    }
}
