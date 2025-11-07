package com.posicube.assignment.common.baseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean isSuccess;
    private String message;
    private String code;
    private HttpStatus httpStatus;
    private T result;

    //성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.httpStatus = BaseResponseStatus.SUCCESS.getHttpStatus();
        this.result = result;
    }

    //실패한 경우
    public BaseResponse(ResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.httpStatus = status.getHttpStatus();
        this.result = null;
    }

    public BaseResponse(ResponseStatus status, String message) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = message;
        this.httpStatus = status.getHttpStatus();
        this.result = null;
    }
}
