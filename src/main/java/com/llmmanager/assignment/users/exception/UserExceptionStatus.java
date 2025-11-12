package com.llmmanager.assignment.users.exception;

import com.llmmanager.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptionStatus implements ResponseStatus {
    /**
     * User 오류
     */
    ACCOUNT_CANNOT_BE_NULL(false, "User-1", "account는 null 일 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_CANNOT_BE_NULL(false, "User-2", "password는 null 일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NAME_CANNOT_BE_NULL(false, "User-3", "name은 null 일 수 없습니다.", HttpStatus.BAD_REQUEST),
    ACCOUNT_CANNOT_CONTAIN_WHITESPACE(false, "User-4", "account는 공백을 포함할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_CANNOT_CONTAIN_WHITESPACE(false, "User-5", "password는 공백을 포함할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NAME_CANNOT_CONTAIN_WHITESPACE(false, "User-6", "name은 공백을 포함할 수 없습니다.", HttpStatus.BAD_REQUEST),
    TOKEN_CANNOT_NULL(false, "User-7", "tokens는 null일 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PLAN_FOR_USER_CREATION(false, "User-8", "사용자 생성에 유효하지 않은 요금제입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(false, "User-9", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_ACCOUNT(false, "User-10", "이미 존재하는 계정입니다.", HttpStatus.CONFLICT),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;
    private final HttpStatus httpStatus;

    UserExceptionStatus(boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

