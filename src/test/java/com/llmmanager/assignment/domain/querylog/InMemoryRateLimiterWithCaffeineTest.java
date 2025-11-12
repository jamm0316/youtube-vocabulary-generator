package com.llmmanager.assignment.domain.querylog;

import com.llmmanager.assignment.querylog.application.service.InMemoryRateLimiterWithCaffeine;
import com.llmmanager.assignment.querylog.application.service.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class InMemoryRateLimiterWithCaffeineTest {

    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new InMemoryRateLimiterWithCaffeine();
    }

    @Test
    @DisplayName("성공: 사용자가 30회까지 요청을 보내면 모두 허용된다.")
    void isAllowed_shouldAllowUpToMaxRequests() {
        // given
        Long userId = 1L;
        int maxRequests = 30;

        // when & then
        for (int i = 0; i < maxRequests; i++) {
            boolean allowed = rateLimiter.isAllowed(userId);
            assertThat(allowed).as("Request #%d should be allowed", i + 1).isTrue();
        }
    }

    @Test
    @DisplayName("실패: 사용자가 30회를 초과하여 요청을 보내면 31번째부터 거부된다.")
    void isAllowed_shouldDenyAfterMaxRequests() {
        // given
        Long userId = 2L;
        int maxRequests = 30;

        // when
        for (int i = 0; i < maxRequests; i++) {
            rateLimiter.isAllowed(userId);
        }
        boolean denied = rateLimiter.isAllowed(userId);

        // then
        assertThat(denied).as("31st request should be denied").isFalse();
    }

    @Test
    @DisplayName("성공: 여러 사용자의 요청은 서로에게 영향을 주지 않는다.")
    void isAllowed_shouldHandleUsersIndependently() {
        // given
        Long userA = 10L;
        Long userB = 11L;
        int maxRequests = 30;

        // when: userA가 30회 요청
        for (int i = 0; i < maxRequests; i++) {
            assertThat(rateLimiter.isAllowed(userA)).isTrue();
        }

        // then: userA는 거부되고, userB는 허용된다.
        assertThat(rateLimiter.isAllowed(userA)).isFalse();
        assertThat(rateLimiter.isAllowed(userB)).isTrue();
    }
}
