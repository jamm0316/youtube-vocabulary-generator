package com.posicube.assignment.querylog.domain;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ModelType {
    GPT5("gpt-5", 0.25), GPT_4O_MINI("gpt-4o-mini", 0.15);

    private final String name;
    private final double pricePer1KToken;

    ModelType(String name, double pricePer1KToken) {
        this.name = name;
        this.pricePer1KToken = pricePer1KToken;
    }

    public static ModelType from(String name) {
        return Arrays.stream(values())
                .filter(modelType -> modelType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(QueryLogExceptionStatus.INVALID_MODEL_TYPE));
    }
}