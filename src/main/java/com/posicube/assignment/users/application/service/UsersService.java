package com.posicube.assignment.users.application.service;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.application.service.PlanService;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.users.domain.model.Users;
import com.posicube.assignment.users.port.UsersRepository;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import com.posicube.assignment.users.application.commandquery.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PlanService planService;

    @Transactional
    public Users createUser(UserCreateRequest request) {
        //1. plan을 찾는다.
        PlanType type = PlanType.from(request.plan());
        Plan plan = planService.findPlanByType(type)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.INVALID_PLAN_FOR_USER_CREATION));

        //2. 해당 plan을 갖는 user를 만든다.
        Users user = Users.create(
                request.account(), request.password(), request.name(), plan);

        return usersRepository.save(user);
    }
}
