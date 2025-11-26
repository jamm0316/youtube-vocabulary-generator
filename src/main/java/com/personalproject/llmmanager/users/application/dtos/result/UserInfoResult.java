package com.personalproject.llmmanager.users.application.dtos.result;

import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.users.domain.model.Tokens;
import com.personalproject.llmmanager.users.domain.model.Users;

public record UserInfoResult(
        String account,
        String name,
        PlanType plan,
        Tokens tokens
) {
    public static UserInfoResult from(Users users) {
        return new UserInfoResult(
                users.getAccount(),
                users.getName(),
                users.getPlanType(),
                users.getTokens()
        );
    }
}
