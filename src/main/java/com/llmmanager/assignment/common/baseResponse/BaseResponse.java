package com.llmmanager.assignment.common.baseResponse;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {
    private boolean isSuccess;
    private String message;
    private String code;
    private HttpStatus httpStatus;
    private T result;

    //성공 응답 (데이터 포함)
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.httpStatus = BaseResponseStatus.SUCCESS.getHttpStatus();
        this.result = result;
    }

    //성공 응답 (ResponseStatus 포함)
    public BaseResponse(ResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.httpStatus = status.getHttpStatus();
        this.result = null;
    }

    //성공 응답 (ResponseStatus 및 커스텀 메시지 포함)
    public BaseResponse(ResponseStatus status, String message) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = message;
        this.httpStatus = status.getHttpStatus();
        this.result = null;
    }
}
