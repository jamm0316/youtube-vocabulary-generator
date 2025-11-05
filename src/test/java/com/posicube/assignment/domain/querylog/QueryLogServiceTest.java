package com.posicube.assignment.domain.querylog;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.common.utils.TokenCalculator;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.querylog.application.QueryLogService;
import com.posicube.assignment.querylog.application.RateLimiter;
import com.posicube.assignment.querylog.domain.port.QueryLogRepository;
import com.posicube.assignment.querylog.domain.vo.QueryLog;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.querylog.presentation.dtos.QueryRequest;
import com.posicube.assignment.querylog.presentation.dtos.QueryResponse;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import com.posicube.assignment.users.domain.vo.Tokens;
import com.posicube.assignment.users.exception.TokenExceptionStatus;
import com.posicube.assignment.users.exception.UserExceptionStatus;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryLogServiceTest {
    @Mock UserRepository userRepository;
    @Mock QueryLogRepository queryLogRepository;
    @Mock LlmClient llmClient;
    @Mock RateLimiter rateLimiter;
    @Mock TokenCalculator tokenCalculator;

    @InjectMocks QueryLogService queryService;

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

    @Test
    @DisplayName("정상 요청 시 LlmClient에서 응답")
    public void submitQuery_llmClientResponse_success() throws Exception {
        //given
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(rateLimiter.isAllowed(1L)).thenReturn(true);
        when(llmClient.query(anyString(), anyString())).thenReturn("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");

        //when
        QueryResponse response = queryService.submitQuery(1L, mockRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.answer()).isEqualTo("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");
    }

    @Test
    @DisplayName("정상 요청 시 토큰을 정확히 차감한다.")
    public void submitQuery_deDuctTokens_success() throws Exception {
        //given
        Tokens tokenMock = mock(Tokens.class);
        when(tokenMock.getRemainingTokens()).thenReturn(1000L);
        ReflectionTestUtils.setField(mockUser, "tokens", tokenMock);
        doNothing().when(mockUser).useTokens(anyLong());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(rateLimiter.isAllowed(1L)).thenReturn(true);
        when(llmClient.query(anyString(), anyString())).thenReturn("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");

        long expectedUsedTokens = new TokenCalculator().calculateTokensFromPrompt(mockRequest.q());
        when(tokenCalculator.calculateTokensFromPrompt(mockRequest.q())).thenReturn(expectedUsedTokens);
        //when
        queryService.submitQuery(1L, mockRequest);

        //then
        verify(mockUser, times(1)).useTokens(expectedUsedTokens);
        verify(queryLogRepository, times(1)).save(any(QueryLog.class));
    }

    @Test
    @DisplayName("잔여 토큰보다 큰 요청도 마지막이라면 성공한다.")
    public void submitQuery_lastRequest_succeeds() throws Exception {
        //given
        Tokens lastTokens = mock(Tokens.class);
        when(lastTokens.getRemainingTokens()).thenReturn(5L);
        ReflectionTestUtils.setField(mockUser, "tokens", lastTokens);
        doNothing().when(mockUser).useTokens(anyLong());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(rateLimiter.isAllowed(1L)).thenReturn(true);
        when(llmClient.query(anyString(), anyString())).thenReturn("i".repeat(50));

        long expectedTokens = tokenCalculator.calculateTokensFromPrompt(mockRequest.q());
        when(tokenCalculator.calculateTokensFromPrompt(mockRequest.q())).thenReturn(expectedTokens);

        //when
        QueryResponse response = queryService.submitQuery(1L, mockRequest);

        //then
        assertThat(response.answer()).isEqualTo("i".repeat(50));
        verify(mockUser, times(1)).useTokens(expectedTokens);
    }

    //todo: 6단계 저장 성공
    //todo: 7단계 호출 실패
}
