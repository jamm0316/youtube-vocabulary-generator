package com.personalproject.llmmanager.querylog.application.dtos.command;

import com.personalproject.llmmanager.querylog.adapter.in.web.request.QueryRequest;

public record QueryCommand(
        String url,
        String model
) {
}
