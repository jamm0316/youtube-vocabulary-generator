package com.posicube.assignment.common.baseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean isSuccess;
    private String message;
    private String code;
    private T result;

    //성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.result = result;
    }

    //실패한 경우
    public BaseResponse(ResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = null;
    }

    public BaseResponse(ResponseStatus status, String message) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = message;
        this.result = null;
    }
}
