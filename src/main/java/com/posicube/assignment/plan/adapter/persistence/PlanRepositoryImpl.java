package com.posicube.assignment.plan.adapter.persistence;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {
    private final SpringDataJpaPlanRepository jpa;

    @Override
    public Plan save(Plan plan) { return jpa.save(plan); }
}
