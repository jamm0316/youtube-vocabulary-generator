package com.posicube.assignment.plan.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public enum PlanExceptionStatus implements ResponseStatus {
    /**
     * Plan 오류
     */
    INVALID_TOTAL_PRICE(false, "PLAN-1", "총 금액이 0이하일 수 없습니다."),
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

