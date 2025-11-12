package com.personalproject.llmmanager.users.application.service;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.plan.application.service.PlanService;
import com.personalproject.llmmanager.plan.domain.model.Plan;
import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.querylog.port.QueryLogRepository;
import com.personalproject.llmmanager.users.application.commandquery.UsageResponse;
import com.personalproject.llmmanager.users.application.commandquery.UserCreateRequest;
import com.personalproject.llmmanager.users.application.commandquery.UserInfoResponse;
import com.personalproject.llmmanager.users.domain.model.Users;
import com.personalproject.llmmanager.users.domain.policy.UsageCalculator;
import com.personalproject.llmmanager.users.domain.policy.UsageSummary;
import com.personalproject.llmmanager.users.exception.UserExceptionStatus;
import com.personalproject.llmmanager.users.port.UsersRepository;
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
