package com.posicube.assignment.common.baseResponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus implements ResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, "BASE-1000", "요청에 성공하였습니다."),

    /**
     * 2000: 요청 오류
     */
    VALIDATION_ERROR(false, "BASE-2001", "요청 데이터가 유효하지 않습니다."),
    NOTFOUND_MEMBER(false, "BASE-2003", "회원이 존재하지 않습니다."),

    /**
     * 3000: 서버 오류
     */
    INTERNAL_SERVER_ERROR(false, "BASE-3000", "서버 오류입니다."),
    ;

    private final boolean isSuccess;
    private final String code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

