package com.posicube.assignment.plan.adapter.out.persistence;

import com.posicube.assignment.plan.domain.model.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    //JPA Entity -> Domain Model
    public Plan toDomain(PlanJpaEntity entity) {
        return Plan.fromPersistenceBuilder()
                .type(entity.getType())
                .updateAt(entity.getUpdateAt())
                .build();
    }

    public PlanJpaEntity toEntity(Plan domain) {
        return PlanJpaEntity.builder()
                .type(domain.getType())
                .updateAt(domain.getUpdateAt())
                .build();
    }
}
