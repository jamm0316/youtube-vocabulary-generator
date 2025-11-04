package com.posicube.assignment.plan.adapter.persistence;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPlanRepository extends JpaRepository<Plan, PlanType> {

}
