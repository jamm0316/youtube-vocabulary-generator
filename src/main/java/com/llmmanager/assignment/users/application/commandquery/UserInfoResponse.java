package com.llmmanager.assignment.users.application.commandquery;

import com.llmmanager.assignment.plan.domain.model.PlanType;
import com.llmmanager.assignment.users.domain.model.Tokens;
import com.llmmanager.assignment.users.domain.model.Users;

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
