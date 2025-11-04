package com.posicube.assignment.plan.domain.port;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    Plan save(Plan plan);
    Optional<Plan> findPlanByType(PlanType type);
    long count();
    List<Plan> saveAll(List<Plan> plans);
}
