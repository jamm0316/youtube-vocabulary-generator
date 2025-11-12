package com.llmmanager.assignment.users.domain.policy;

import com.llmmanager.assignment.querylog.domain.model.ModelType;

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
