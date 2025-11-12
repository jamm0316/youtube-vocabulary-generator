package com.llmmanager.assignment.users.application.service;

import com.llmmanager.assignment.common.exception.BaseException;
import com.llmmanager.assignment.plan.application.service.PlanService;
import com.llmmanager.assignment.plan.domain.model.Plan;
import com.llmmanager.assignment.plan.domain.model.PlanType;
import com.llmmanager.assignment.querylog.domain.model.QueryLog;
import com.llmmanager.assignment.querylog.port.QueryLogRepository;
import com.llmmanager.assignment.users.application.commandquery.UsageResponse;
import com.llmmanager.assignment.users.application.commandquery.UserCreateRequest;
import com.llmmanager.assignment.users.application.commandquery.UserInfoResponse;
import com.llmmanager.assignment.users.domain.model.Users;
import com.llmmanager.assignment.users.domain.policy.UsageCalculator;
import com.llmmanager.assignment.users.domain.policy.UsageSummary;
import com.llmmanager.assignment.users.exception.UserExceptionStatus;
import com.llmmanager.assignment.users.port.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final QueryLogRepository queryLogRepository;
    private final PlanService planService;
    private final UsageCalculator usageCalculator;

    @Transactional
    public Users createUser(UserCreateRequest request) {
        //1. 기존 사용자인지 확인한다.
        usersRepository.findUsersByAccount(request.account()).ifPresent(user -> {
            throw new BaseException(UserExceptionStatus.DUPLICATE_ACCOUNT);
        });

        //2. plan을 찾는다.
        PlanType type = PlanType.from(request.plan());
        Plan plan = planService.findPlanByType(type)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.INVALID_PLAN_FOR_USER_CREATION));

        //3. 해당 plan을 갖는 user를 만든다.
        Users user = Users.create(
                request.account(), request.password(), request.name(), plan);

        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UsageResponse getUserUsage(Long userId) {
        Users user = usersRepository.findUserById(userId)
                .orElseThrow(() -> new BaseException(UserExceptionStatus.USER_NOT_FOUND));
        List<QueryLog> queryLogs = queryLogRepository.findAllByUserId(userId);
        UsageSummary usageSummary = usageCalculator.calculate(user, queryLogs);
        return UsageResponse.from(usageSummary);
    }

    public List<UserInfoResponse> findAllUsersInfo() {
        return usersRepository.findAllUsersInfo();
    }
}
