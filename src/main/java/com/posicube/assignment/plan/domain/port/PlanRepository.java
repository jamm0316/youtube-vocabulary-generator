package com.posicube.assignment.plan.domain.port;

import com.posicube.assignment.plan.domain.entity.Plan;

public interface PlanRepository {
    Plan save(Plan plan);
}
