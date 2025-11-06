package com.posicube.assignment.users.domain.policy;

import com.posicube.assignment.querylog.domain.model.ModelType;
import com.posicube.assignment.querylog.domain.model.QueryLog;
import com.posicube.assignment.users.domain.model.Users;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UsageCalculator {

    private static final BigDecimal THOUSAND = new BigDecimal("1000");
    private static final int PRICE_SCALE = 2;

    /**
     * 사용자의 전체 사용량 요약 정보를 계산합니다.
     * 쿼리 로그 모델 타입별로 집계하여 총 토큰 사용량, 비용, 잔여 토큰을 계산합니다.
     *
     * @param user      사용자 정보
     * @param queryLogs 쿼리 로그 목록
     * @return 사용량 요약 정보 (플랜, 할당량, 사용 토큰, 잔여 토큰, 총 가격, 모델별 사용 내역)
     */
    public UsageSummary calculate(Users user, List<QueryLog> queryLogs) {
        Map<ModelType, ModelUsageDetails> modelUsageDetailsMap = calculateModelUsageDetails(queryLogs);

        long totalTokens = sumTotalTokens(modelUsageDetailsMap);
        BigDecimal totalPrice = sumTotalPrice(modelUsageDetailsMap);

        long quota = user.getTokens().getQuota();
        long remainingToken = calculateRemainingTokens(quota, totalTokens);

        return new UsageSummary(
                user.getPlanType().name(),
                quota,
                totalTokens,
                remainingToken,
                totalPrice,
                modelUsageDetailsMap
        );
    }

    /**
     * 쿼리 로그를 모델 타입별로 그룹화하여 각 모델의 사용 내역을 계산합니다.
     * 동일한 모델 타입의 로그들을 하나의 ModelUsageDetails로 집계합니다.
     *
     * @param queryLogs 쿼리 로그 목록
     * @return 모델 타입별 사용 내역 맵
     */
    private Map<ModelType, ModelUsageDetails> calculateModelUsageDetails(List<QueryLog> queryLogs) {
        return queryLogs.stream()
                .collect(Collectors.groupingBy(
                        QueryLog::getType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::createModelUsageDetails)
                ));
    }

    /**
     * 동일한 모델 타입의 쿼리 로그들로부터 모델 사용 내역을 생성합니다.
     * 총 토큰 수를 합산하고 해당 모델 가격 정책에 따라 비용을 계산합니다.
     *
     * @param logs 동일 모델 타입의 쿼리 로그 목록
     * @return 모델 사용 내역(모델명, 총 토큰 수, 가격)
     */
    private ModelUsageDetails createModelUsageDetails(List<QueryLog> logs) {
        ModelType type = logs.get(0).getType();
        long totalTokens = sumTokens(logs);
        BigDecimal price = calculatePrice(type, totalTokens);

        return new ModelUsageDetails(type.getName(), totalTokens, price);
    }

    /**
     * 쿼리 로그 목록의 총 토큰 수를 합산합니다.
     *
     * @param logs 쿼리 로그 목록
     * @return 총 사용 토큰 수
     */
    private long sumTokens(List<QueryLog> logs) {
        return logs.stream()
                .mapToLong(QueryLog::getUsedTokens)
                .sum();
    }

    /**
     * 모델별 사용 내역에서 전체 토큰 수를 합산합니다.
     *
     * @param modelUsageDetailsMap 모델 타입별 사용 내역 맵
     * @return 전체 사용 토큰 수
     */
    private long sumTotalTokens(Map<ModelType, ModelUsageDetails> modelUsageDetailsMap) {
        return modelUsageDetailsMap.values().stream()
                .mapToLong(ModelUsageDetails::tokens)
                .sum();
    }

    /**
     * 모델별 사용 내역에서 전체 가격을 합산합니다.
     * 합산 후 소수점 2자리에서 반올림 합니다.
     *
     * @param modelUsageDetailsMap 모델 타입별 사용 내역 맵
     * @return 총 사용 금액(소수점 2자리)
     */
    private BigDecimal sumTotalPrice(Map<ModelType, ModelUsageDetails> modelUsageDetailsMap) {
        return modelUsageDetailsMap.values().stream()
                .map(ModelUsageDetails::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(PRICE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 잔여 토큰 수를 계산합니다.
     * 계산 공식: 할당량 - 사용량 (결과가 음수인 경우 0 반환)
     *
     * @param quota      할당된 토큰 수
     * @param usedTokens 사용한 토큰 수
     * @return 잔여 토큰 수
     */
    private long calculateRemainingTokens(long quota, long usedTokens) {
        return Math.max(quota - usedTokens, 0);
    }

    /**
     * 특정 모델의 사용 토큰에 대한 가격을 계산합니다.
     * 계산 공식: (총 토큰 수 / 1000) * 모델의 1K당 가격
     * 중간 계산은 소수점 10자리, 최종 결과는 소수점 2자리에서 반올림합니다.
     *
     * @param type        모델 타입
     * @param totalTokens 총 토큰 수
     * @return 계산된 가격(소수점 2자리)
     */
    private BigDecimal calculatePrice(ModelType type, long totalTokens) {
        return BigDecimal.valueOf(totalTokens)
                .divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(type.getPricePer1KToken())
                .setScale(PRICE_SCALE, RoundingMode.HALF_UP);
    }
}
