package com.personalproject.llmmanager.querylog.adapter.in.web.request;

import com.personalproject.llmmanager.querylog.application.dtos.command.QueryCommand;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record QueryRequest (
        @NotBlank(message = "url은 null이거나 비어있을 수 없습니다.")
        @URL
        String url,

        @NotBlank(message = "모델 타입은 null이거나 비어있을 수 없습니다.")
        String model
) {
    public QueryCommand toCommand() {
        return new QueryCommand(this.url, this.model);
    }
}
