package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.plan.domain.model.PlanType;

public record UserTokenResetDto(
        Long id,
        PlanType planType,
        String account,
        String password,
        String name,
        Long quota
) {

}
