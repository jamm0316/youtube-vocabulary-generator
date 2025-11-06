package com.posicube.assignment.querylog.application.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class InMemoryRateLimiter implements RateLimiter {
    private static final int MAX_REQUEST = 30;
    private static final int TIME_WINDOW_IN_MINUTE = 1;

    private final Map<Long, Deque<LocalDateTime>> requestTimestamp = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(TIME_WINDOW_IN_MINUTE);

        // 1. 해당 사용자의 요청 기록 큐를 가져온다. 없으면 새로 생성
        Deque<LocalDateTime> userTimestamp = requestTimestamp.computeIfAbsent(userId, k -> new ConcurrentLinkedDeque<>());

        // 2. 동기화 블록으로 특정 사용자의 큐에 대한 동시 접근을 막는다.
        synchronized (userTimestamp) {
            //3. 1분 이전의 오래된 요청 기록들을 큐에서 제거 (Sliding Window)
            while (!userTimestamp.isEmpty() && userTimestamp.peekFirst().isBefore(windowStart)) {
                userTimestamp.pollFirst();
            }

            //4. 현재 윈도우 내의 요청 횟수가 30회 미만인지 확인
            if (userTimestamp.size() < MAX_REQUEST) {
                userTimestamp.add(now);
                return true;
            }
        }
        return false;
    }
}
