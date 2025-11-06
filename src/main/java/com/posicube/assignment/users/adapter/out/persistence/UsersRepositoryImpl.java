package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.adapter.out.persistence.PlanJpaEntity;
import com.posicube.assignment.plan.adapter.out.persistence.SpringDataJpaPlanRepository;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import com.posicube.assignment.users.application.commandquery.UserInfoResponse;
import com.posicube.assignment.users.domain.model.Users;
import com.posicube.assignment.users.port.UsersRepository;
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

    public List<UserInfoResponse> findAllUsersInfo() {
        return jpaUsersRepository.findAllUsersInfo();
    }
}
