package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.plan.adapter.out.persistence.PlanJpaEntity;
import com.posicube.assignment.users.domain.model.Tokens;
import com.posicube.assignment.users.domain.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    //JPA Entity -> Domain Model
    public Users toDomain(UsersJpaEntity entity) {
        Tokens tokens = new Tokens(entity.getQuota(), entity.getUsedTokens(), entity.getRemainingTokens());
        return Users.fromPersistenceBuilder()
                .id(entity.getId())
                .planType(entity.getPlan().getType())
                .account(entity.getAccount())
                .password(entity.getPassword())
                .name(entity.getName())
                .tokens(tokens)
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
                .quota(domain.getTokens().getQuota())
                .usedTokens(domain.getTokens().getUsedTokens())
                .remainingTokens(domain.getTokens().getRemainingTokens())
                .build();
    }
}
