package com.posicube.assignment.querylog.domain.vo;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ModelType {
    GPT5("gpt-5", "0.25"), GPT_4O_MINI("gpt-4o-mini", "0.15");

    private final String name;
    private final BigDecimal pricePer1KToken;
    private static final Map<String, ModelType> stringToEnum
            = Stream.of(values())
            .collect(Collectors.toMap(ModelType::getName, Function.identity()));

    ModelType(String name, String pricePer1KToken) {

        this.name = name;
        this.pricePer1KToken = new BigDecimal(pricePer1KToken);
    }

    public static ModelType from(String name) {
        return Optional.ofNullable(name)
                .map(String::toLowerCase)
                .map(stringToEnum::get)
                .orElseThrow(() -> new BaseException(QueryLogExceptionStatus.INVALID_MODEL_TYPE));
    }
}