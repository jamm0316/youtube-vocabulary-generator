package com.posicube.assignment.querylog.application.facade;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.application.commandquery.QueryRequest;
import com.posicube.assignment.querylog.application.commandquery.QueryResponse;
import com.posicube.assignment.querylog.application.service.QueryLogService;
import com.posicube.assignment.querylog.application.service.RateLimiter;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryLogFacade {
    private final QueryLogService queryLogService;
    private final RateLimiter rateLimiter;

    private static final int MAX_RETRY = 5;
    private static final long RETRY_DELAY_MS = 50;

    public QueryResponse submitQuery(Long userId, QueryRequest request) {
        // 1. Rate Limit 먼저 체크 (재시도 전)
        if (!rateLimiter.isAllowed(userId)) {
            throw new BaseException(QueryLogExceptionStatus.TOO_MANY_REQUESTS);
        }

        // 2. 낙관 락 재시도 로직
        int attempt = 0;
        OptimisticLockException lastException = null;

        while (attempt < MAX_RETRY) {
            try {
                return queryLogService.submitQuery(userId, request);
            } catch (OptimisticLockException e) {
                lastException = e;
                attempt++;

                try {
                    Thread.sleep(RETRY_DELAY_MS * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }

        //3. 재시도 실패 시
        throw new BaseException(QueryLogExceptionStatus.TOO_MANY_CONCURRENT_REQUESTS);
    }
}
