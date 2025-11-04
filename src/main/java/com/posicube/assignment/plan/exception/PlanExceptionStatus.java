package com.posicube.assignment.plan.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public enum PlanExceptionStatus implements ResponseStatus {
    /**
     * Plan 오류
     */
    PLAN_TYPE_CANNOT_BE_NULL(false, "PLAN-1", "PLAN_TYPE은 null 일 수 없습니다."),
    INVALID_TOTAL_PRICE(false, "PLAN-2", "총 금액이 0이하일 수 없습니다."),
    CANNOT_CHANGE_SAME_TYPE(false, "PLAN-3", "같은 플랜으로 변경할 수 없습니다."),
    TOKENS_CANNOT_BE_NULL(false, "PLAN-4", "토큰 정보는 필수입니다."),
    INSUFFICIENT_TOKENS(false, "PLAN-5", "잔여 토큰이 부족합니다."),
    INVALID_PLAN_TYPE(false, "PLAN-6", "유효하지 않은 플랜 타입입니다."),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;

    PlanExceptionStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

