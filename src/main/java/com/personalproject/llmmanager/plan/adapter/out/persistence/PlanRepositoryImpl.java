package com.personalproject.llmmanager.plan.adapter.out.persistence;

import com.personalproject.llmmanager.plan.domain.model.Plan;
import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.plan.port.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {
    private final SpringDataJpaPlanRepository jpaPlanRepository;
    private final PlanMapper planMapper;

    @Override
    public Plan save(Plan plan) {
        PlanJpaEntity savedPlan = jpaPlanRepository.save(planMapper.toEntity(plan));
        return planMapper.toDomain(savedPlan);
    }

    @Override
    public Optional<Plan> findPlanByType(PlanType type) {
        return jpaPlanRepository.findPlanByType(type)
                .map(planMapper::toDomain);
    }

    @Override
    public long count() {
        return jpaPlanRepository.count();
    }

    @Override
    public List<Plan> saveAll(List<Plan> plans) {
        List<PlanJpaEntity> entityList = plans.stream()
                .map(planMapper::toEntity)
                .collect(Collectors.toList());
        return jpaPlanRepository.saveAll(entityList).stream()
                .map(planMapper::toDomain)
                .collect(Collectors.toList());
    }
}
