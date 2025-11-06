package com.posicube.assignment.plan.adapter.out.persistence;

import com.posicube.assignment.plan.domain.model.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPlanRepository extends JpaRepository<PlanJpaEntity, PlanType> {
    Optional<PlanJpaEntity> findPlanByType(PlanType type);
}
