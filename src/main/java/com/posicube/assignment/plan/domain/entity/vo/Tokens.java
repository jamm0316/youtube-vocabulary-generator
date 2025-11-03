package com.posicube.assignment.plan.domain.entity.vo;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.PlanType;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import jakarta.persistence.Embeddable;

@Embeddable
public record Tokens (
        long quota,
        long usedTokens,
        long remainingTokens
) {
    public static Tokens create(PlanType planType) {
        long initialQuota = planType.getQuota();
        return new Tokens(
                initialQuota,
                0L,
                initialQuota
        );
    }

    public Tokens use(long tokensToUse) {
        if (remainingTokens < tokensToUse) {
            throw new BaseException(PlanExceptionStatus.INSUFFICIENT_TOKENS);
        }

        long newUsedTokens = usedTokens + tokensToUse;
        long newRemainingTokens = remainingTokens - tokensToUse;
        return new Tokens(quota, newUsedTokens, newRemainingTokens);
    }
}
