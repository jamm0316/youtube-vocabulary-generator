package com.personalproject.llmmanager.users.domain.policy;

import com.personalproject.llmmanager.querylog.domain.model.ModelType;

import java.math.BigDecimal;
import java.util.Map;

public record UsageSummary(
        String plan,
        long quota,
        long usedTokens,
        long remainingTokens,
        BigDecimal totalPrice,
        Map<ModelType, ModelUsageDetails> modelUsageDetails
) {
}
