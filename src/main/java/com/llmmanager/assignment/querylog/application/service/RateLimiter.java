package com.llmmanager.assignment.querylog.application.service;

public interface RateLimiter {
    /**
     * 해당 사용자의 요청이 허용되는지 확인합니다.
     * @param userId 사용자 ID
     * @return 허용되면 true, 거부되면 false
     */
    boolean isAllowed(Long userId);
}
