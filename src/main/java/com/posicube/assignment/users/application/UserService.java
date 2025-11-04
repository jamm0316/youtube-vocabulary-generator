package com.posicube.assignment.users.application;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.application.PlanService;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
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
        Plan plan = planService.findPlanByType(request.plan())
                .orElseThrow(() -> new BaseException(PlanExceptionStatus.INVALID_PLAN_TYPE));
        Users users = Users.create(
                request.account(), request.password(), request.name(), plan);
        return userRepository.save(users);
    }
}
