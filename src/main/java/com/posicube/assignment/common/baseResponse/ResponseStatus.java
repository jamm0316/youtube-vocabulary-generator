package com.posicube.assignment.common.baseResponse;

public interface ResponseStatus {
    boolean isSuccess();

    String getCode();

    String getMessage();
}
