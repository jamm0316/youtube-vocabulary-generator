package com.posicube.assignment.users.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public enum TokenExceptionStatus implements ResponseStatus {
    /**
     * Token 오류
     */
    INSUFFICIENT_TOKENS(false, "Token-1", "잔여 토큰이 부족합니다."),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;

    TokenExceptionStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

