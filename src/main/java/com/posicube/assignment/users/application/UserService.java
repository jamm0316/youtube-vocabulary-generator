package com.posicube.assignment.users.application;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.application.PlanService;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import com.posicube.assignment.users.presentation.dtos.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PlanService planService;

    @Transactional
    public Users createUser(UserCreateRequest request) {
        PlanType type = PlanType.from(request.plan());
        Plan plan = planService.findPlanByType(type)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.INVALID_PLAN_FOR_USER_CREATION));
        Users users = Users.create(
                request.account(), request.password(), request.name(), plan);
        return userRepository.save(users);
    }
}
