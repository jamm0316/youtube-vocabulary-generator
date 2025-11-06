package com.posicube.assignment.querylog.adapter.out.persistence;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.querylog.port.out.QueryLogRepository;
import com.posicube.assignment.users.adapter.out.persistence.SpringDataJpaUsersRepository;
import com.posicube.assignment.users.adapter.out.persistence.UsersJpaEntity;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QueryLogRepositoryImpl implements QueryLogRepository {
    private final SpringDataJpaQueryLogRepository jpaQueryLogRepository;
    private final SpringDataJpaUsersRepository jpaUserRepository;
    private final QueryLogMapper queryLogMapper;

    @Override
    public QueryLog save(QueryLog queryLog) {
        UsersJpaEntity userEntity = jpaUserRepository.findUsersById(queryLog.getUserId())
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        QueryLogJpaEntity entityToSave = queryLogMapper.toEntity(queryLog, userEntity);

        QueryLogJpaEntity savedEntity = jpaQueryLogRepository.save(entityToSave);
        return queryLogMapper.toDomain(savedEntity);
    }

    @Override
    public List<QueryLog> findAllByUserId(Long userId) {
        UsersJpaEntity userEntity = jpaUserRepository.findUsersById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));

        List<QueryLogJpaEntity> allByUser = jpaQueryLogRepository.findAllByUser(userEntity);

        return allByUser.stream()
                .map(queryLogMapper::toDomain)
                .collect(Collectors.toList());
    }
}
