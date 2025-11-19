package com.personalproject.llmmanager.querylog.exception;

import com.personalproject.llmmanager.common.baseResponse.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QueryLogExceptionStatus implements ResponseStatus {
    /**
     * QueryLog 오류
     */
    URL_CANNOT_BE_NULL(false, "QUERY_LOG-1", "URL은 null이거나 비어있을 수 없습니다.", HttpStatus.BAD_REQUEST),
    MODEL_TYPE_CANNOT_BE_NULL(false, "QUERY_LOG-2", "모델 타입은 null이거나 비어있을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_MODEL_TYPE(false, "QUERY_LOG-3", "유효하지 않은 모델 타입입니다.", HttpStatus.BAD_REQUEST),
    USER_CANNOT_BE_NULL(false, "QUERY_LOG-4", "사용자 정보는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    QUERY_TOO_LONG(false, "QUERY_LOG-5", "쿼리는 1자 이상 800자 이내로 작성해야합니다.", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUESTS(false, "QUERY_LOG-6", "1분 이내 요청 30개를 초과할 수 없습니다.", HttpStatus.TOO_MANY_REQUESTS),
    USED_TOKEN_CANNOT_BE_NULL(false, "QUERY_LOG-7", "사용된 토큰은 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    LLM_API_ERROR(false, "QUERY_LOG-8", "LLM API 동작 중 에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ANSWER_CANNOT_BE_NULL(false, "QUERY_LOG-9", "답변이 null일 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    TOO_MANY_CONCURRENT_REQUESTS(false, "QUERY_LOG-10", "동시 요청이 많습니다. 잠시 후 다시 시도해주세요.", HttpStatus.TOO_MANY_REQUESTS),
    ;

    private final boolean isSuccess;
    private final String  code;
    private final String message;
    private final HttpStatus httpStatus;

    QueryLogExceptionStatus(boolean isSuccess, String code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

