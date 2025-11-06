package com.posicube.assignment.plan.application.service;

import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.plan.port.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    /**
     * PlanType으로 Plan 도메인 객체를 조회합니다.
     * @param type 조회할 요금제 타입
     * @return Plan 도메인 객체(Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Plan> findPlanByType(PlanType type) {
        return planRepository.findPlanByType(type);
    }
}
