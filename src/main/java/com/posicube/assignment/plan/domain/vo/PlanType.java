package com.posicube.assignment.plan.domain.vo;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum PlanType {
    LITE(10_000L), PRO( 50_000L);

    private final long quota;

    PlanType(long quota) {
        this.quota = quota;
    }

    private static final Map<String, PlanType> stringToEnum
            = Stream.of(values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static PlanType from(String name) {
        return Optional.ofNullable(name)
                .map(String::toUpperCase)
                .map(stringToEnum::get)
                .orElseThrow(() -> new BaseException(PlanExceptionStatus.INVALID_PLAN_TYPE));
    }
}
