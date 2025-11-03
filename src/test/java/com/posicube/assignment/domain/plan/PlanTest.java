package com.posicube.assignment.domain.plan;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.entity.PlanType;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlanTest {

    @Test
    @DisplayName("생성 성공:PlanType.LITE 정상 생성")
    public void create_plan_success() throws Exception {
        //given
        Plan lite = Plan.createLite();

        //then
        assertThat(lite.getType()).isEqualTo(PlanType.LITE);
        assertThat(lite.getTokens().quota()).isEqualTo(PlanType.LITE.getQuota());
        assertThat(lite.getTokens().remainingTokens()).isEqualTo(PlanType.LITE.getQuota());
    }

    @Test
    @DisplayName("changePlanType 메서드 성공: PlanType 변경 성공")
    public void change_plan_type_success() throws Exception {
        //given
        Plan plan = Plan.createLite();

        //when&then
        plan.changePlanType(PlanType.PRO);
        assertThat(plan.getType()).isEqualTo(PlanType.PRO);
        assertThat(plan.getTokens().quota()).isEqualTo(PlanType.PRO.getQuota());
        assertThat(plan.getTokens().remainingTokens()).isEqualTo(PlanType.PRO.getQuota());

        plan.changePlanType(PlanType.LITE);
        assertThat(plan.getType()).isEqualTo(PlanType.LITE);
        assertThat(plan.getTokens().quota()).isEqualTo(PlanType.LITE.getQuota());
        assertThat(plan.getTokens().remainingTokens()).isEqualTo(PlanType.LITE.getQuota());
    }

    @Test
    @DisplayName("changePlanType 메서드 실패: PlanType이 같으면 예외 발생")
    public void update_plan_success() throws Exception {
        //given
        Plan plan = Plan.createLite();

        //when&then
        assertThatThrownBy(() -> plan.changePlanType(PlanType.LITE))
                .isInstanceOf(BaseException.class)
                .hasMessage(PlanExceptionStatus.CANNOT_CHANGE_SAME_TYPE.getMessage());
    }
}

