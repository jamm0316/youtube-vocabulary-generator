package com.personalproject.llmmanager.users.adapter.in.web.request;

import com.personalproject.llmmanager.users.application.dtos.command.UserCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "account는 null 일 수 없습니다.")
        @Size(min = 1, max = 100, message = "account는 1자 이상 100자 이하이어야 합니다.")
        String account,

        @NotBlank(message = "password는 null 일 수 없습니다.")
        @Size(min = 1, max = 100, message = "password는 1자 이상 100자 이하이어야 합니다.")
        String password,

        @NotBlank(message = "name은 null 일 수 없습니다.")
        @Size(min = 1, max = 30, message = "name은 1자 이상 30 이하이어야 합니다.")
        String name,

        @NotBlank(message = "planType은 null 일 수 없습니다.")
        String plan
) {
    public UserCreateCommand toCommand() {
        return new UserCreateCommand(this.account, this.password, this.name, this.plan);
    }
}
