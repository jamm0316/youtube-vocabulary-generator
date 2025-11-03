package com.posicube.assignment.common.exception;

import com.posicube.assignment.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ResponseStatus status;

    public BaseException(ResponseStatus baseResponseStatus) {
        super(baseResponseStatus.getMessage());
        this.status = baseResponseStatus;
    }
}
