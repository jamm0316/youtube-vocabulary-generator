package com.personalproject.llmmanager.users.application.dtos.result;

import com.personalproject.llmmanager.users.domain.policy.ModelUsageDetails;
import com.personalproject.llmmanager.users.domain.policy.UsageSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record UsageResult(
        String plan,
        long quota,
        long usedTokens,
        long remainingTokens,
        BigDecimal totalPrice,
        List<ModelUsageDetails> models
) {
    public static UsageResult from(UsageSummary summary) {
        List<ModelUsageDetails> modelUsageDetailsList =
                summary.modelUsageDetails().values().stream().collect(Collectors.toList());

        return new UsageResult(
                summary.plan(),
                summary.quota(),
                summary.usedTokens(),
                summary.remainingTokens(),
                summary.totalPrice(),
                modelUsageDetailsList
        );
    }
}
