package com.posicube.assignment.users.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public enum UserExceptionStatus implements ResponseStatus {
    /**
     * User 오류
     */
    ACCOUNT_CANNOT_BE_NULL(false, "User-1", "account는 null 일 수 없습니다."),
    PASSWORD_CANNOT_BE_NULL(false, "User-2", "password는 null 일 수 없습니다."),
    NAME_CANNOT_BE_NULL(false, "User-3", "name은 null 일 수 없습니다."),
    ACCOUNT_CANNOT_CONTAIN_WHITESPACE(false, "User-4", "account는 공백을 포함할 수 없습니다."),
    PASSWORD_CANNOT_CONTAIN_WHITESPACE(false, "User-5", "password는 공백을 포함할 수 없습니다."),
    NAME_CANNOT_CONTAIN_WHITESPACE(false, "User-6", "name은 공백을 포함할 수 없습니다."),
    TOKEN_CANNOT_NULL(false, "User-7", "tokens는 null일 수 없습니다."),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;

    UserExceptionStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

