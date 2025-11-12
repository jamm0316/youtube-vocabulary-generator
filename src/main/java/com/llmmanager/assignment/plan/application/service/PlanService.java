package com.llmmanager.assignment.plan.application.service;

import com.llmmanager.assignment.plan.domain.model.Plan;
import com.llmmanager.assignment.plan.domain.model.PlanType;
import com.llmmanager.assignment.plan.port.PlanRepository;
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

    @Transactional(readOnly = true)
    public Optional<Plan> findPlanByType(PlanType type) {
        return planRepository.findPlanByType(type);
    }
}
