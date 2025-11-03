package com.posicube.assignment.plan.domain.entity;

import lombok.Getter;

@Getter
public enum PlanType {
    LITE(10_000L), PRO(50_000L);

    private final long quota;

    PlanType(long quota) {
        this.quota = quota;
    }
}
