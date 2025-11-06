package com.posicube.assignment.querylog.adapter.out.persistence;

import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.users.adapter.out.persistence.UsersJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class QueryLogMapper {

    //JPA Entity -> Domain Model
    public QueryLog toDomain(QueryLogJpaEntity entity) {
        return QueryLog.fromPersistenceBuilder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .type(entity.getType())
                .content(entity.getContent())
                .answer(entity.getAnswer())
                .usedTokens(entity.getUsedTokens())
                .cost(entity.getCost())
                .createAt(entity.getCreateAt())
                .build();
    }

    //Domain Model -> JPA Entity
    public QueryLogJpaEntity toEntity(QueryLog domain, UsersJpaEntity usersJpaEntity) {
        return QueryLogJpaEntity.builder()
                .id(domain.getId())
                .user(usersJpaEntity)
                .type(domain.getType())
                .content(domain.getContent())
                .answer(domain.getAnswer())
                .usedTokens(domain.getUsedTokens())
                .cost(domain.getCost())
                .createAt(domain.getCreateAt())
                .build();
    }
}
