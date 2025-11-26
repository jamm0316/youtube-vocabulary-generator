package com.personalproject.llmmanager.users.adapter.in.web.response;

import com.personalproject.llmmanager.users.application.dtos.result.UsageResult;
import com.personalproject.llmmanager.users.domain.policy.ModelUsageDetails;

import java.math.BigDecimal;
import java.util.List;

public record UsageResponse (
        String plan,
        long quota,
        long usedTokens,
        long remainingTokens,
        BigDecimal totalPrice,
        List<ModelUsageDetails> models
) {
    static public UsageResponse from(UsageResult result) {
        return new UsageResponse(
                result.plan(),
                result.quota(),
                result.usedTokens(),
                result.remainingTokens(),
                result.totalPrice(),
                result.models()
        );
    }
}
