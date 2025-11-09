package com.posicube.assignment.domain.users;

import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.users.domain.model.Tokens;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TokenTest {

    private Plan litePlan;
    private Plan proPlan;
    @BeforeEach
    void setUp() {
        litePlan = Plan.create(PlanType.LITE);
        proPlan = Plan.create(PlanType.PRO);
    }

    @Test
    @DisplayName("성공: LITE 요금제의 초기 토큰을 생성한다.")
    public void initialOf_litePlan_succeeds() {
        // when
        Tokens tokens = Tokens.initialOf(litePlan);

        // then
        assertThat(tokens.getQuota()).isEqualTo(PlanType.LITE.getQuota());
        assertThat(tokens.getUsedTokens()).isZero();
        assertThat(tokens.getRemainingTokens()).isEqualTo(PlanType.LITE.getQuota());
    }

    @Test
    @DisplayName("성공: PRO 요금제의 초기 토큰을 생성한다.")
    public void initialOf_proPlan_succeeds() {
        // when
        Tokens tokens = Tokens.initialOf(proPlan);

        // then
        assertThat(tokens.getQuota()).isEqualTo(PlanType.PRO.getQuota());
        assertThat(tokens.getUsedTokens()).isZero();
        assertThat(tokens.getRemainingTokens()).isEqualTo(PlanType.PRO.getQuota());
    }

    @Test
    @DisplayName("성공: 토큰을 정상적으로 차감한다.")
    public void deduct_succeeds() {
        // given
        Tokens initialTokens = Tokens.initialOf(litePlan);
        long amountToUse = 1000L;

        // when
        Tokens newTokens = initialTokens.deduct(amountToUse);

        // then
        assertThat(newTokens.getUsedTokens()).isEqualTo(amountToUse);
        assertThat(newTokens.getRemainingTokens()).isEqualTo(initialTokens.getQuota() - amountToUse);
        assertThat(newTokens.getQuota()).isEqualTo(initialTokens.getQuota());

        // 원본 객체는 불변이어야 한다.
        assertThat(initialTokens.getUsedTokens()).isZero();
        assertThat(initialTokens.getRemainingTokens()).isEqualTo(initialTokens.getQuota());
    }

    @Test
    @DisplayName("성공: 남은 토큰보다 많은 양을 사용하면 남은 토큰은 0이 된다.")
    public void deduct_moreThanRemaining_succeeds() {
        // given
        Tokens initialTokens = Tokens.initialOf(litePlan);
        long amountToUse = PlanType.LITE.getQuota() + 1000L;

        // when
        Tokens newTokens = initialTokens.deduct(amountToUse);

        // then
        assertThat(newTokens.getUsedTokens()).isEqualTo(initialTokens.getUsedTokens() + amountToUse);
        assertThat(newTokens.getRemainingTokens()).isZero();
    }
    @Test
    @DisplayName("성공: 토큰을 여러 번 차감해도 정확히 계산된다.")
    public void deduct_multipleTimes_succeeds() {
        // given
        Tokens initialTokens = Tokens.initialOf(proPlan);
        long firstAmount = 5000L;
        long secondAmount = 10000L;

        // when
        Tokens firstDeduction = initialTokens.deduct(firstAmount);
        Tokens secondDeduction = firstDeduction.deduct(secondAmount);

        // then
        long totalUsed = firstAmount + secondAmount;
        assertThat(secondDeduction.getUsedTokens()).isEqualTo(totalUsed);
        assertThat(secondDeduction.getRemainingTokens()).isEqualTo(initialTokens.getQuota() - totalUsed);

        // 첫 번째 차감 후 객체는 변하지 않는다.
        assertThat(firstDeduction.getUsedTokens()).isEqualTo(firstAmount);
        assertThat(firstDeduction.getRemainingTokens()).isEqualTo(initialTokens.getQuota() - firstAmount);
    }
}
