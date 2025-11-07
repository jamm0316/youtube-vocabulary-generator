package com.posicube.assignment.domain.plan;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlanTest {
    private Plan plan;

    @BeforeEach
    public void setUp() {
        //given
        PlanType lite = PlanType.LITE;

        //when
        plan = Plan.create(lite);
    }

    @Test
    @DisplayName("생성 성공: 정상 생성")
    public void create_plan_success() {
        //given
        PlanType lite = PlanType.LITE;

        //when
        Plan plan = Plan.create(lite);

        //then
        assertThat(plan.getType()).isEqualTo(PlanType.LITE);
    }

    @Test
    @DisplayName("생성 실패: PlanType이 null이면 예외 반환")
    public void create_plan_fail() {
        //when&then
        assertThatThrownBy(() -> Plan.create(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL.getMessage());
    }

    @Test
    @DisplayName("changePlanType 메서드 성공: PlanType 변경 성공")
    public void change_plan_type_success() {
        //when&then
        plan.changePlanType(PlanType.PRO);
        assertThat(plan.getType()).isEqualTo(PlanType.PRO);

        plan.changePlanType(PlanType.LITE);
        assertThat(plan.getType()).isEqualTo(PlanType.LITE);
    }

    @Test
    @DisplayName("changePlanType 메서드 실패: PlanType이 같거나 null이면 예외 발생")
    public void update_plan_success() {
        //when&then
        assertThatThrownBy(() -> plan.changePlanType(PlanType.LITE))
                .isInstanceOf(BaseException.class)
                .hasMessage(PlanExceptionStatus.CANNOT_CHANGE_SAME_TYPE.getMessage());

        assertThatThrownBy(() -> plan.changePlanType(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL.getMessage());
    }
}

