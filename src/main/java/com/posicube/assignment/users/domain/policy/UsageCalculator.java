package com.posicube.assignment.users.domain.policy;

import com.posicube.assignment.querylog.domain.model.ModelType;
import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.users.domain.model.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsageCalculator {

    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    public UsageSummary calculate(Users user, List<QueryLog> queryLogs) {
        long usedTokens = calculateUsedTokens(queryLogs);
        long quota = user.getTokens().getQuota();
        long remainingToken = calculateRemainingTokens(quota, usedTokens);
        Map<ModelType, ModelUsageDetails> modelUsageDetailsMap = calculateModelUsageDetails(queryLogs);
        BigDecimal totalPrice = calculateTotalPrice(modelUsageDetailsMap);

        return new UsageSummary(
                user.getPlanType().name(),
                quota,
                usedTokens,
                remainingToken,
                totalPrice,
                modelUsageDetailsMap
        );
    }

    private long calculateUsedTokens(List<QueryLog> queryLogs) {
        return queryLogs.stream()
                .mapToLong(QueryLog::getUsedTokens)
                .sum();
    }

    private long calculateRemainingTokens(long quota, long usedTokens) {
        long remaining = quota - usedTokens;
        return Math.max(remaining, 0);
    }

    private Map<ModelType, ModelUsageDetails> calculateModelUsageDetails(List<QueryLog> queryLogs) {
        return queryLogs.stream()
                .collect(Collectors.groupingBy(
                        QueryLog::getType,
                        Collectors.collectingAndThen(Collectors.toList(), this::createModelUsageDetails)
                ));
    }

    private ModelUsageDetails createModelUsageDetails(List<QueryLog> logs) {
        ModelType type = logs.get(0).getType();
        long totalModelTokens = calculateUsedTokens(logs);
        BigDecimal modelPrice = calculatePrice(type, totalModelTokens);
        return new ModelUsageDetails(type.getName(), totalModelTokens, modelPrice);
    }

    private BigDecimal calculatePrice(ModelType modelType, long totalTokens) {
        BigDecimal pricePer1KToken = modelType.getPricePer1KToken();
        BigDecimal tokenBigDecimal = BigDecimal.valueOf(totalTokens);
        return tokenBigDecimal.divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(pricePer1KToken)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalPrice(Map<ModelType, ModelUsageDetails> modelUsageDetails) {
        return modelUsageDetails.values().stream()
                .map(ModelUsageDetails::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
