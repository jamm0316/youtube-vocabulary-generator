package com.posicube.assignment.plan.domain.vo;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum PlanType {
    LITE("LITE", 10_000L), PRO("PRO", 50_000L);

    private final String name;
    private final long quota;

    PlanType(String name, long quota) {
        this.name = name;
        this.quota = quota;
    }

    public static PlanType from(String name) {
        return Arrays.stream(values())
                .filter(planType -> planType.name.toUpperCase(Locale.ROOT).equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(PlanExceptionStatus.INVALID_PLAN_TYPE));
    }
}
