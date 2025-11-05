package com.posicube.assignment.querylog.presentation.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QueryRequest (
        @NotBlank(message = "질의 내용은 null이거나 비어있을 수 없습니다.")
        @Size(max = 800, message = "쿼리는 1자 이상 800자 이내로 작성해야합니다.")
        String q,

        @NotBlank(message = "모델 타입은 null이거나 비어있을 수 없습니다.")
        String model
) {
}
