package com.llmmanager.assignment.querylog.adapter.out.persistence;

import com.llmmanager.assignment.querylog.domain.model.QueryLog;
import com.llmmanager.assignment.users.adapter.out.persistence.UsersJpaEntity;
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
                .createAt(domain.getCreateAt())
                .build();
    }
}
