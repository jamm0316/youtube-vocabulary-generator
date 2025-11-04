package com.posicube.assignment.plan.adapter.persistence;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPlanRepository extends JpaRepository<Plan, PlanType> {
    Optional<Plan> findPlanByType(PlanType type);
}
