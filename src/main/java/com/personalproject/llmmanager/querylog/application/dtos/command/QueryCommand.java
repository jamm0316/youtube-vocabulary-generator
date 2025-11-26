package com.personalproject.llmmanager.querylog.application.dtos.command;

public record QueryCommand(
        String url,
        String model
) {
}
