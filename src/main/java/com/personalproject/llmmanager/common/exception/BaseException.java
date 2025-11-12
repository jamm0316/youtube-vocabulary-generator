package com.personalproject.llmmanager.common.exception;

import com.personalproject.llmmanager.common.baseResponse.ResponseStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ResponseStatus status;

    public BaseException(ResponseStatus baseResponseStatus) {
        super(baseResponseStatus.getMessage());
        this.status = baseResponseStatus;
    }
}
