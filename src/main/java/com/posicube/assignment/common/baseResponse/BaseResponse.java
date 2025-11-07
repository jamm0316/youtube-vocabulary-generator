package com.posicube.assignment.common.baseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    //성공 응답
    public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
        return ResponseEntity.ok(new BaseResponse<>(data));
    }

    //성공 응답 (상태 코드 지정)
    public static <T> ResponseEntity<BaseResponse<T>> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(new BaseResponse<>(data));
    }

    //실패 응답
    public static ResponseEntity<BaseResponse<Object>> error(ResponseStatus status) {
        return ResponseEntity.status(status.getHttpStatus())
                .body(new BaseResponse<>(status));
    }

    //실패 응답 (메시지 커스텀)
    public static ResponseEntity<BaseResponse<Object>> error(ResponseStatus status, String message) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(status, message));
    }
}
