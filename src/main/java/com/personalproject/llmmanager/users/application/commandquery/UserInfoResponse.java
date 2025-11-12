package com.personalproject.llmmanager.users.application.commandquery;

import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.users.domain.model.Tokens;
import com.personalproject.llmmanager.users.domain.model.Users;

public record UserInfoResponse (
        String account,
        String name,
        PlanType plan,
        Tokens tokens
) {
    public static UserInfoResponse from(Users users) {
        return new UserInfoResponse(
                users.getAccount(),
                users.getName(),
                users.getPlanType(),
                users.getTokens()
        );
    }
}
