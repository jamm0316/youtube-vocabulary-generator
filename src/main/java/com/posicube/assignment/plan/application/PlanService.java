package com.posicube.assignment.plan.application;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import com.posicube.assignment.plan.domain.vo.PlanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    public Optional<Plan> findPlanByType(String name) {
        return planRepository.findPlanByType(PlanType.from(name));
    }
}
