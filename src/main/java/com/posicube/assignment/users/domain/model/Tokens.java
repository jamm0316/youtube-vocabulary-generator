package com.posicube.assignment.users.domain.model;

import com.posicube.assignment.plan.domain.model.Plan;
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

    public void deduct(long amountToUse) {
        usedTokens += amountToUse;
        remainingTokens -= amountToUse;
        if (remainingTokens < 0) {
            remainingTokens = 0;
        }
    }
}
