package com.posicube.assignment.users.domain.vo;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.users.exception.TokenExceptionStatus;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tokens {
    long quota;
    long usedTokens;
    long remainingTokens;

    private Tokens(long quota) {
        this.quota = quota;
        usedTokens = 0;
        this.remainingTokens = quota;
    }

    private Tokens(long quota, long usedTokens, long remainingTokens) {
        this.quota = quota;
        this.usedTokens = usedTokens;
        this.remainingTokens = remainingTokens;
    }

    public static Tokens initialOf(Plan plan) {
        return new Tokens(plan.getType().getQuota());
    }

    public Tokens use(long tokensToUse) {
        if (this.remainingTokens < tokensToUse) {
            throw new BaseException(TokenExceptionStatus.INSUFFICIENT_TOKENS);
        }

        long newUsedTokens = usedTokens + tokensToUse;
        long newRemainingTokens = remainingTokens - tokensToUse;
        return new Tokens(quota, newUsedTokens, newRemainingTokens);
    }
}
