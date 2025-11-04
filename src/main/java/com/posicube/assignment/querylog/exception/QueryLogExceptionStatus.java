package com.posicube.assignment.querylog.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public enum QueryLogExceptionStatus implements ResponseStatus {
    /**
     * QueryLog 오류
     */
    QUERY_CANNOT_BE_NULL(false, "QUERY_LOG-1", "질의 내용은 null이거나 비어있을 수 없습니다."),
    MODEL_TYPE_CANNOT_BE_NULL(false, "QUERY_LOG-2", "모델 타입은 null이거나 비어있을 수 없습니다."),
    INVALID_MODEL_TYPE(false, "QUERY_LOG-3", "유효하지 않은 모델 타입입니다."),
    USER_CANNOT_BE_NULL(false, "QUERY_LOG-4", "사용자 정보는 null일 수 없습니다."),
    QUERY_TOO_LONG(false, "QUERY_LOG-5", "쿼리는 1자 이상 800자 이내로 작성해야합니다."),
    TOO_MANY_REQUESTS(false, "QUERY_LOG-6", "1분 이내 요청 30개를 초과할 수 없습니다."),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;

    QueryLogExceptionStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

