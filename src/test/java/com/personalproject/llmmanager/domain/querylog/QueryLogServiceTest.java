package com.personalproject.llmmanager.domain.querylog;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.plan.domain.model.Plan;
import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.querylog.application.dtos.command.QueryCommand;
import com.personalproject.llmmanager.querylog.application.dtos.result.QueryResult;
import com.personalproject.llmmanager.querylog.application.facade.QueryLogFacade;
import com.personalproject.llmmanager.querylog.application.service.QueryLogService;
import com.personalproject.llmmanager.querylog.application.service.RateLimiter;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.querylog.domain.policy.TokenCalculator;
import com.personalproject.llmmanager.querylog.exception.QueryLogExceptionStatus;
import com.personalproject.llmmanager.querylog.port.LlmPort;
import com.personalproject.llmmanager.querylog.port.QueryLogRepository;
import com.personalproject.llmmanager.users.domain.model.Tokens;
import com.personalproject.llmmanager.users.domain.model.Users;
import com.personalproject.llmmanager.users.exception.TokenExceptionStatus;
import com.personalproject.llmmanager.users.exception.UserExceptionStatus;
import com.personalproject.llmmanager.users.port.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryLogServiceTest {
    @Mock
    UsersRepository usersRepository;
    @Mock
    QueryLogRepository queryLogRepository;
    @Mock
    LlmPort llmPort;
    @Mock
    RateLimiter rateLimiter;
    @Mock
    TokenCalculator tokenCalculator;

    @InjectMocks
    QueryLogService queryService;
    @InjectMocks
    QueryLogFacade queryLogFacade;

    private Users mockUser;
    private QueryCommand mockRequest;

    @BeforeEach
    void setUp() {
        mockUser = spy(Users.create("testUser", "pass1234", "evan", Plan.create(PlanType.LITE)));
        mockRequest = new QueryCommand("test prompt", "gpt-5");
    }

    @Test
    @DisplayName("submitQuery: 사용자 조회 실패: 존재하지 않는 사용자면 예외를 던진다.")
    public void submitQuery_userNotFound_fail() {
        //given: ID조회 시 결과 없음
        when(usersRepository.findUserById(anyLong())).thenReturn(Optional.empty());

        //when&then: 서비스 실행 시 예외 검증
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(UserExceptionStatus.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("submitQuery: Rate Limit를 초과하면 예외를 던진다.")
    public void submitQuery_rateLimit_fail() {
        //given: RateLimiter가 항상 요청 거부
        when(rateLimiter.isAllowed(1L)).thenReturn(false);

        //when&then: 서비스 실행 시 예외 검증
        assertThatThrownBy(() -> queryLogFacade.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.TOO_MANY_REQUESTS.getMessage());
    }

    @Test
    @DisplayName("submitQuery: 잔여 토큰이 없으면 예외를 던진다.")
    public void submitQuery_insufficientTokens_fail() {
        //given: 토큰이 0인 mock Token 객체 주입
        Tokens zeroToken = mock(Tokens.class);
        when(zeroToken.getRemainingTokens()).thenReturn(0L);
        ReflectionTestUtils.setField(mockUser, "tokens", zeroToken);

        when(usersRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));

        //then: 서비스 실행 시 예외 검증
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(TokenExceptionStatus.INSUFFICIENT_TOKENS.getMessage());
    }

    @Test
    @DisplayName("정상 요청 시 LlmClient에서 응답")
    public void submitQuery_llmClientResponse_success() {
        //given: 모든 의존성이 정상적으로 동작하도록 설정
        when(usersRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(llmPort.query(anyString(), anyString())).thenReturn("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");
        when(queryLogRepository.save(any(QueryLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        QueryResult result = queryService.submitQuery(1L, mockRequest);

        //then
        assertThat(result).isNotNull();
        assertThat(result.answer()).isEqualTo("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");
    }

    @Test
    @DisplayName("정상 요청 시 토큰을 정확히 차감한다.")
    public void submitQuery_deDuctTokens_success() {
        //given: 실제 Users(spy) 객체와 그 안의 실제 Tokens 객체 사용
        long initialRemainingTokens = mockUser.getTokens().getRemainingTokens();
        long expectedUsedTokens = new TokenCalculator().calculateTokensFromPrompt(mockRequest.url());

        when(usersRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(llmPort.query(anyString(), anyString())).thenReturn("모델 gpt-5 로부터의 응답: query 에 대한 답변입니다.");
        when(tokenCalculator.calculateTokensFromPrompt(mockRequest.url())).thenReturn(expectedUsedTokens);
        when(queryLogRepository.save(any(QueryLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        QueryResult result = queryService.submitQuery(1L, mockRequest);

        //then
        verify(mockUser, times(1)).useTokens(expectedUsedTokens);

        long expectedRemainingTokens = initialRemainingTokens - expectedUsedTokens;
        assertThat(result.remainingToken()).isEqualTo(expectedRemainingTokens);
    }

    @Test
    @DisplayName("잔여 토큰보다 큰 요청도 마지막이라면 성공한다.")
    public void submitQuery_lastRequest_succeeds() {
        //given: 잔여 토큰이 매우 적은 상황 설정
        Tokens realTokens = new Tokens(10000L, 9995L, 5L);
        Users realUser = Users.fromPersistenceBuilder()
                .id(1L)
                .account("testUser")
                .password("pass1234")
                .name("evan")
                .planType(PlanType.LITE)
                .tokens(realTokens)
                .build();

        Users spy = spy(realUser);

        long expectedUsedTokens = 75L;

        when(usersRepository.findUserById(1L)).thenReturn(Optional.of(spy));
        when(llmPort.query(anyString(), anyString())).thenReturn("i".repeat(100));
        when(queryLogRepository.save(any(QueryLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenCalculator.calculateTokensFromPrompt(mockRequest.url())).thenReturn(expectedUsedTokens);

        //when: 서비스 실행
        QueryResult result = queryService.submitQuery(1L, mockRequest);

        //then
        //1. LLM 호출 확인
        assertThat(result.answer()).isEqualTo("i".repeat(100));
        //2. useTokens 메서드가 1번 호출됐는지 확인
        verify(spy, times(1)).useTokens(expectedUsedTokens);
        //3. 잔여토큰이 0인지 확인(음수x)
        assertThat(result.remainingToken()).isEqualTo(0L);
    }

    @Test
    @DisplayName("LLM 클라이언트 호출 실패 시 예외 발생 및 토큰 미차감")
    public void submitQuery_llmClient_fail() {
        //given: LLM 클라이언트 호출 시 예외 발생 설정
        when(usersRepository.findUserById(1L)).thenReturn(Optional.of(mockUser));
        when(llmPort.query(anyString(), anyString())).thenThrow(new RuntimeException("외부 API 호출 중 오류가 발생했습니다"));

        //when&then: 서비스 실행 시 예외 발생 검증
        assertThatThrownBy(() -> queryService.submitQuery(1L, mockRequest))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.LLM_API_ERROR.getMessage());

        //then: 예외 발생 후 토큰 차감 및 로그 저장 로직 호출 검증
        verify(mockUser, never()).useTokens(anyLong());
        verify(queryLogRepository, never()).save(any(QueryLog.class));
    }
}
