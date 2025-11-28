package com.personalproject.llmmanager.query.exception;

import com.personalproject.llmmanager.common.baseResponse.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QueryExceptionStatus implements ResponseStatus {
    /**
     * QueryLog 오류
     */
    TOKEN_CALCULATION_IS_REQUIRED(false, "QUERY-1", "토큰 계산이 먼저 필요합니다.", HttpStatus.BAD_REQUEST),
    QUERY_IS_NOT_COMPLETE(false, "QUERY-2", "쿼리가 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
    USER_CANNOT_BE_NULL(false, "QUERY-3", "user가 null이면 안됩니다.", HttpStatus.BAD_REQUEST),
    URL_CANNOT_BE_NULL_OR_BLANK(false, "QUERY-4", "url이 null이면 안됩니다.", HttpStatus.BAD_REQUEST),
    MODEL_TYPE_CANNOT_BE_NULL(false, "QUERY-4", "url이 null이면 안됩니다.", HttpStatus.BAD_REQUEST),
    TOKEN_CALCULATION_IS_ONLY_POSSIBLE_IN_PENDING(false, "QUERY-4", "url이 null이면 안됩니다.", HttpStatus.BAD_REQUEST),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;
    private final HttpStatus httpStatus;

    QueryExceptionStatus(boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

