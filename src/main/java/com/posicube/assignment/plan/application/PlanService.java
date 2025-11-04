package com.posicube.assignment.plan.application;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import com.posicube.assignment.plan.domain.vo.PlanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    @Transactional
    public Plan createPlan(String type) {
        Plan plan = Plan.create(PlanType.from(type));
        return planRepository.save(plan);
    }
}
