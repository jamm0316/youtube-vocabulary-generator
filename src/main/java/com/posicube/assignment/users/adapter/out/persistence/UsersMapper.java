package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.plan.adapter.out.persistence.PlanJpaEntity;
import com.posicube.assignment.users.domain.model.Tokens;
import com.posicube.assignment.users.domain.model.Users;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    //JPA Entity -> Domain Model
    public Users toDomain(UsersJpaEntity entity) {
        return Users.builder()
                .id(entity.getId())
                .planType(entity.getPlan().getType())
                .account(entity.getAccount())
                .password(entity.getPassword())
                .name(entity.getName())
                .tokens(entity.getTokens())
                .build();
    }

    //Domain Model -> JPA Entity
    public UsersJpaEntity toEntity(Users domain, PlanJpaEntity planJpaEntity) {
        return UsersJpaEntity.builder()
                .id(domain.getId())
                .plan(planJpaEntity)
                .account(domain.getAccount())
                .password(domain.getPassword())
                .name(domain.getName())
                .tokens(domain.getTokens())
                .build();
    }
}
