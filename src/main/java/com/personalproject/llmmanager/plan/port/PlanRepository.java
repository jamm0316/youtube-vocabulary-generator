package com.personalproject.llmmanager.plan.port;

import com.personalproject.llmmanager.plan.domain.model.Plan;
import com.personalproject.llmmanager.plan.domain.model.PlanType;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    Plan save(Plan plan);
    Optional<Plan> findPlanByType(PlanType type);
    long count();
    List<Plan> saveAll(List<Plan> plans);
}
