package com.personalproject.llmmanager.users.adapter.in.web.response;

import com.personalproject.llmmanager.plan.domain.model.PlanType;
import com.personalproject.llmmanager.users.application.dtos.result.UserInfoResult;
import com.personalproject.llmmanager.users.domain.model.Tokens;

public record UserInfoResponse (
        String account,
        String name,
        PlanType plan,
        Tokens tokens
) {
    public static UserInfoResponse from(UserInfoResult users) {
        return new UserInfoResponse(
                users.account(),
                users.name(),
                users.plan(),
                users.tokens()
        );
    }
}
