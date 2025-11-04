package com.posicube.assignment.users.presentation.dtos;

import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.vo.Tokens;

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
                users.getPlan().getType(),
                users.getTokens()
        );
    }
}
