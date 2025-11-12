package com.llmmanager.assignment.plan.port;

import com.llmmanager.assignment.plan.domain.model.Plan;
import com.llmmanager.assignment.plan.domain.model.PlanType;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    Plan save(Plan plan);
    Optional<Plan> findPlanByType(PlanType type);
    long count();
    List<Plan> saveAll(List<Plan> plans);
}
