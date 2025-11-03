package com.posicube.assignment.users.presentation.dtos;

import com.posicube.assignment.plan.domain.entity.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "account는 null 일 수 없습니다.")
        @Size(min = 3, max = 100, message = "account는 3자 이상 100자 이하이어야 합니다.")
        String account,

        @NotBlank(message = "password는 null 일 수 없습니다.")
        @Size(min = 8, max = 100, message = "password는 8자 이상 100자 이하이어야 합니다.")
        String password,

        @NotBlank(message = "name은 null 일 수 없습니다.")
        @Size(min = 8, max = 30, message = "name은 1자 이상 30 이하이어야 합니다.")
        String name,

        PlanType plan

) {

    public static UserCreateRequest create(String account, String password, String name, PlanType plan) {
        return new UserCreateRequest(account, password, name, plan);
    }
}
