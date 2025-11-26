package com.personalproject.llmmanager.users.application.dtos.command;

public record UserCreateCommand(
        String account,
        String password,
        String name,
        String plan
) {

}
