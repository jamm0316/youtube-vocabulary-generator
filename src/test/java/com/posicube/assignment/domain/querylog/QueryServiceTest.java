package com.posicube.assignment.domain.querylog;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.querylog.application.QueryService;
import com.posicube.assignment.querylog.application.RateLimiter;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.querylog.presentation.dtos.QueryRequest;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import com.posicube.assignment.users.domain.vo.Tokens;
import com.posicube.assignment.users.exception.TokenExceptionStatus;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryServiceTest {
    @Mock UserRepository userRepository;
    @Mock LlmClient llmClient;
    @Mock RateLimiter rateLimiter;
    @InjectMocks QueryService queryService;

    private Users mockUser;
    private QueryRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockUser = spy(Users.create("testUser", "pass1234", "evan", Plan.create(PlanType.LITE)));
        mockRequest = new QueryRequest("test prompt", "gpt-5");
    }

    @Test
    @DisplayName("submitQuery: 사용자 조회 실패: 존재하지 않는 사용자면 예외를 던진다.")
    public void submitQuery_userNotFound_fail() throws Exception {
        //given
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("submitQuery: Rate Limit를 초과하면 예외를 던진다.")
    public void submitQuery_rateLimit_fail() throws Exception {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(rateLimiter.isAllowed(1L)).thenReturn(false);

        //when&then
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.TOO_MANY_REQUESTS.getMessage());
    }

    @Test
    @DisplayName("submitQuery: 잔여 토큰이 없으면 예외를 던진다.")
    public void submitQuery_insufficientTokens_fail() throws Exception {
        //given
        Tokens zeroToken = mock(Tokens.class);
        when(zeroToken.getRemainingTokens()).thenReturn(0L);

        ReflectionTestUtils.setField(mockUser, "tokens", zeroToken);

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(rateLimiter.isAllowed(1L)).thenReturn(true);

        //then
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(TokenExceptionStatus.INSUFFICIENT_TOKENS.getMessage());
    }
}
