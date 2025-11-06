package com.posicube.assignment.users.application.commandquery;

import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.users.domain.model.Tokens;
import com.posicube.assignment.users.domain.model.Users;

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
