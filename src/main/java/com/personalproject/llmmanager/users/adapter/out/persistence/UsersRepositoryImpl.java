package com.personalproject.llmmanager.users.adapter.out.persistence;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.plan.adapter.out.persistence.PlanJpaEntity;
import com.personalproject.llmmanager.plan.adapter.out.persistence.SpringDataJpaPlanRepository;
import com.personalproject.llmmanager.plan.exception.PlanExceptionStatus;
import com.personalproject.llmmanager.users.application.commandquery.UserInfoResponse;
import com.personalproject.llmmanager.users.domain.model.Users;
import com.personalproject.llmmanager.users.port.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {
    private final SpringDataJpaUsersRepository jpaUsersRepository;
    private final SpringDataJpaPlanRepository jpaPlanRepository;
    private final UsersMapper mapper;

    @Override
    public Users save(Users users) {
        PlanJpaEntity planJpaEntity = jpaPlanRepository.findPlanByType(users.getPlanType())
                .orElseThrow(() -> new BaseException(PlanExceptionStatus.INVALID_PLAN_TYPE));

        UsersJpaEntity entity = mapper.toEntity(users, planJpaEntity);
        return mapper.toDomain(jpaUsersRepository.save(entity));
    }

    @Override
    public Optional<Users> findUserById(Long id) {
        return jpaUsersRepository.findUsersById(id)
                .map(mapper::toDomain);
    }

    @Override
    public int resetAllUserTokens() {
        return jpaUsersRepository.resetAllUserTokens();
    }

    @Override
    public List<UserInfoResponse> findAllUsersInfo() {
        return jpaUsersRepository.findAllUsersInfo();
    }

    @Override
    public Optional<Users> findUsersByAccount(String account) {
        return jpaUsersRepository.findUsersByAccount(account)
                .map(mapper::toDomain);
    }
}
