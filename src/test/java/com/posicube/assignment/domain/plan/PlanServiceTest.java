package com.posicube.assignment.domain.plan;

import com.posicube.assignment.plan.application.service.PlanService;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.plan.port.PlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    @Test
    @DisplayName("성공: 요금제 종류(PlanType)로 조회하면 요금제(Plan)를 반환한다.")
    void findPlanByType_returnsPlan_whenFound() {
        // given
        PlanType liteType = PlanType.LITE;
        Plan mockPlan = Plan.create(liteType);
        when(planRepository.findPlanByType(liteType)).thenReturn(Optional.of(mockPlan));

        // when
        Optional<Plan> foundPlanOpt = planService.findPlanByType(liteType);

        // then
        assertThat(foundPlanOpt).isPresent();
        assertThat(foundPlanOpt.get().getType()).isEqualTo(liteType);
    }

    @Test
    @DisplayName("성공: 존재하지 않는 요금제 종류로 조회하면 빈 Optional을 반환한다.")
    void findPlanByType_returnsEmpty_whenNotFound() {
        // given
        PlanType proType = PlanType.PRO;
        when(planRepository.findPlanByType(proType)).thenReturn(Optional.empty());

        // when
        Optional<Plan> foundPlanOpt = planService.findPlanByType(proType);

        // then
        assertThat(foundPlanOpt).isNotPresent();
    }

}
