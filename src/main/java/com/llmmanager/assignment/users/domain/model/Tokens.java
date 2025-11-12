package com.llmmanager.assignment.users.domain.model;

import com.llmmanager.assignment.plan.domain.model.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tokens {
    private final long quota;
    private final long usedTokens;
    private final long remainingTokens;

    public static Tokens initialOf(Plan plan) {
        return new Tokens(plan.getType().getQuota(), 0L, plan.getType().getQuota());
    }

    public Tokens deduct(long amountToUse) {
        long newUsedTokens = this.usedTokens + amountToUse;
        long newRemainingTokens = this.remainingTokens - amountToUse;
        if (newRemainingTokens < 0) {
            newRemainingTokens = 0;
        }
        return new Tokens(this.quota, newUsedTokens, newRemainingTokens);
    }
}
