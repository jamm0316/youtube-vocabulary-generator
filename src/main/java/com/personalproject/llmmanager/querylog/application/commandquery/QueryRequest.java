package com.personalproject.llmmanager.querylog.application.commandquery;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record QueryRequest (
        @NotBlank(message = "url은 null이거나 비어있을 수 없습니다.")
        @URL
        String url,

        @NotBlank(message = "모델 타입은 null이거나 비어있을 수 없습니다.")
        String model
) {
}
