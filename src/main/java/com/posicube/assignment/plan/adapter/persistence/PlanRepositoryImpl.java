package com.posicube.assignment.plan.adapter.persistence;

import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.port.PlanRepository;
import com.posicube.assignment.plan.domain.vo.PlanType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {
    private final SpringDataJpaPlanRepository jpa;

    public Plan save(Plan plan) { return jpa.save(plan); }

    public Optional<Plan> findPlanByType(PlanType type) {
        return jpa.findPlanByType(type);
    }

    public long count() {
        return jpa.count();
    }

    public List<Plan> saveAll(List<Plan> plans) {
        return jpa.saveAll(plans);
    }
}
