package com.llmmanager.assignment.common.baseResponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus implements ResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, "BASE-1000", "요청에 성공하였습니다.", HttpStatus.OK),

    /**
     * 2000: 요청 오류
     */
    VALIDATION_ERROR(false, "BASE-2001", "요청 데이터가 유효하지 않습니다.", HttpStatus.NOT_FOUND),

    /**
     * 3000: 서버 오류
     */
    INTERNAL_SERVER_ERROR(false, "BASE-3000", "서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    BaseResponseStatus(boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

