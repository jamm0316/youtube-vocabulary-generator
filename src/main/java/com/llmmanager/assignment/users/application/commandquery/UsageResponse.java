package com.llmmanager.assignment.users.application.commandquery;

import com.llmmanager.assignment.users.domain.policy.ModelUsageDetails;
import com.llmmanager.assignment.users.domain.policy.UsageSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record UsageResponse (
        String plan,
        long quota,
        long usedTokens,
        long remainingTokens,
        BigDecimal totalPrice,
        List<ModelUsageDetails> models
) {
    public static UsageResponse from(UsageSummary summary) {
        List<ModelUsageDetails> modelUsageDetailsList =
                summary.modelUsageDetails().values().stream().collect(Collectors.toList());

        return new UsageResponse(
                summary.plan(),
                summary.quota(),
                summary.usedTokens(),
                summary.remainingTokens(),
                summary.totalPrice(),
                modelUsageDetailsList
        );
    }
}
