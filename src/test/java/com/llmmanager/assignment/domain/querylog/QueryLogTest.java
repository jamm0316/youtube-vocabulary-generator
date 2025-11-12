package com.llmmanager.assignment.domain.querylog;

import com.llmmanager.assignment.common.exception.BaseException;
import com.llmmanager.assignment.plan.domain.model.Plan;
import com.llmmanager.assignment.plan.domain.model.PlanType;
import com.llmmanager.assignment.querylog.domain.model.ModelType;
import com.llmmanager.assignment.querylog.domain.model.QueryLog;
import com.llmmanager.assignment.querylog.domain.policy.TokenCalculator;
import com.llmmanager.assignment.querylog.exception.QueryLogExceptionStatus;
import com.llmmanager.assignment.users.domain.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QueryLogTest {
    private Users testUser;
    private TokenCalculator tokenCalculator;

    @BeforeEach
    void setUp() {
        testUser = Users.create(
                "evanbackeng@gmail.com", "pass1234", "evan", Plan.create(PlanType.PRO)
        );
        tokenCalculator = new TokenCalculator();
    }

    @Test
    @DisplayName("성공: GPT-5 모델의 토큰과 비용을 정확히 계산한다.")
    public void createQuery_withGpt5_calculatesTokensAndCostCorrectly() {
        //given
        String prompt = "안녕하세요? 이번 포지큐브 백엔드 주니어 개발자에 지원하게 된 송재명 입니다. 100자에 맞춰 프롬프트를 작성하면 GPT-5모델은 5L의 토큰과 0.02의 비용이 청구될 것입니다.";
        String answer = "GPT-5 모델의 답변입니다.";
        ModelType gpt5 = ModelType.GPT5;
        Long expectedTokens = new TokenCalculator().calculateTokensFromPrompt(prompt);

        BigDecimal bigDecimal = new BigDecimal(expectedTokens);
        BigDecimal THOUSAND = new BigDecimal(1000);
        BigDecimal costBefoeRounding = bigDecimal.divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(gpt5.getPricePer1KToken());
        BigDecimal expectedCost = costBefoeRounding.setScale(2, RoundingMode.HALF_UP);

        //when
        QueryLog queryLog = QueryLog.create(testUser, prompt, gpt5, answer, expectedTokens);

        //then
        assertThat(queryLog).isNotNull();
        assertThat(queryLog.getUserId()).isEqualTo(testUser.getId());
        assertThat(queryLog.getType()).isEqualTo(ModelType.GPT5);
        assertThat(queryLog.getContent()).isEqualTo(prompt);
        assertThat(queryLog.getUsedTokens()).isEqualTo(expectedTokens);
        assertThat(queryLog.getAnswer()).isEqualTo(answer);
    }

    @Test
    @DisplayName("성공: GPT-4o-mini 모델의 토큰과 비용을 정확히 계산한다.")
    public void createQuery_withGpt4iMini_calculatesTokensAndCostCorrectly() {
        //given
        String prompt = "안녕하세요? 이번 포지큐브 백엔드 주니어 개발자에 지원하게 된 송재명 입니다. 100자에 맞춰 프롬프트를 작성하면 GPT-4모델은 5L의 토큰과 0.02의 비용이 청구될 것입니다.";
        String answer = "GPT-4o-mini 모델의 답변입니다.";
        ModelType gpt4oMini = ModelType.GPT_4O_MINI;
        Long expectedTokens = new TokenCalculator().calculateTokensFromPrompt(prompt);

        BigDecimal bigDecimal = new BigDecimal(expectedTokens);
        BigDecimal THOUSAND = new BigDecimal(1000);
        BigDecimal costBefoeRounding = bigDecimal.divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(gpt4oMini.getPricePer1KToken());
        BigDecimal expectedCost = costBefoeRounding.setScale(2, RoundingMode.HALF_UP);

        //when
        QueryLog queryLog = QueryLog.create(testUser, prompt, gpt4oMini, answer, expectedTokens);

        //then
        assertThat(queryLog).isNotNull();
        assertThat(queryLog.getUserId()).isEqualTo(testUser.getId());
        assertThat(queryLog.getType()).isEqualTo(ModelType.GPT_4O_MINI);
        assertThat(queryLog.getContent()).isEqualTo(prompt);
        assertThat(queryLog.getUsedTokens()).isEqualTo(expectedTokens);
        assertThat(queryLog.getAnswer()).isEqualTo(answer);
    }

    @Test
    @DisplayName("성공: 토큰 계산 시 소수접을 정확히 반올림 한다.")
    public void createQueryLog_roundsTokensCorrectly() {
        //given
        String promptRoundingUpFromHalf = "Length 999";  //10자 -> 10 * 0.75 = 7.5 -> 8
        String promptRoundingDownFromQuarter = "Length 1000"; //11자 -> 11 * 0.75 = 8.25 -> 8
        String answer = "반올림 테스트 답변.";
        Long expectedTokensHalf = new TokenCalculator().calculateTokensFromPrompt(promptRoundingUpFromHalf);
        Long expectedTokensQuarter = new TokenCalculator().calculateTokensFromPrompt(promptRoundingDownFromQuarter);


        //when
        QueryLog queryLogRoundingUp = QueryLog.create(
                testUser, promptRoundingUpFromHalf, ModelType.GPT5, answer, expectedTokensHalf);
        QueryLog queryLogRoundingDown = QueryLog.create(
                testUser, promptRoundingDownFromQuarter, ModelType.GPT5, answer, expectedTokensQuarter);

        //then
        assertThat(queryLogRoundingUp.getUsedTokens()).isEqualTo(8L);
        assertThat(queryLogRoundingDown.getUsedTokens()).isEqualTo(8L);
    }

    @Test
    @DisplayName("실패: User가 null이면 예외를 발생시킨다.")
    public void createQueryLog_withNullUser_fail() {
        //given
        String prompt = "User가 null이면 예외를 발생시킨다.";
        String answer = "답변입니다.";
        Long usedTokens = new TokenCalculator().calculateTokensFromPrompt(prompt);

        //when&then
        assertThatThrownBy(() -> QueryLog.create(null, prompt, ModelType.GPT5, answer, usedTokens))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.USER_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("실패: 질의 내용(q)이 null 이거나 비어 있으면 예외를 발생시킨다.")
    public void createQueryLog_withNullBlankQuery_fail() {
        //given
        String prompt = " ";
        String answer = "답변입니다.";
        Long usedTokens = tokenCalculator.calculateTokensFromPrompt(prompt);

        //when&then
        assertThatThrownBy(() -> QueryLog.create(testUser, null, ModelType.GPT5, answer, null))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL.getMessage());

        assertThatThrownBy(() -> QueryLog.create(testUser, prompt, ModelType.GPT5, answer, usedTokens))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("실패: ModelType이 null 이거나 없는 모델이 들어오면 예외를 발생시킨다.")
    public void createQueryLog_withNullModelType_fail() {
        //given
        String prompt = "ModelType이 null 이면 예외를 발생시킨다.";
        String answer = "답변입니다.";
        Long usedTokens = tokenCalculator.calculateTokensFromPrompt(prompt);

        //when&then
        assertThatThrownBy(() -> QueryLog.create(testUser, prompt, null, answer, usedTokens))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("실패: usedTokens이 null이면 예외를 발생시킨다.")
    public void createQuery_withNullUsedTokens_fail() {
        //given
        String prompt = "usedToken이 null이면 예외를 발생시킨다.";
        String answer = "답변입니다.";

        //when&then
        assertThatThrownBy(() -> QueryLog.create(testUser, prompt, ModelType.GPT5, answer, null))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.USED_TOKEN_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("실패: 질의 내용(q)이 800자를 초과하면 예외를 발생시킨다.")
    public void createQuery_withTooLongQuery_fail() {
        //given
        String longPrompt = "글".repeat(801);
        String answer = "답변입니다.";
        Long usedToken = tokenCalculator.calculateTokensFromPrompt(longPrompt);

        //when&then
        assertThatThrownBy(() -> QueryLog.create(testUser, longPrompt, ModelType.GPT5, answer, usedToken))
                .isInstanceOf(BaseException.class)
                .hasMessage(QueryLogExceptionStatus.QUERY_TOO_LONG.getMessage());
    }
}
