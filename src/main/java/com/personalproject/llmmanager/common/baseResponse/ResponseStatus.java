package com.personalproject.llmmanager.common.baseResponse;

import org.springframework.http.HttpStatus;

public interface ResponseStatus {
    boolean isSuccess();
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
