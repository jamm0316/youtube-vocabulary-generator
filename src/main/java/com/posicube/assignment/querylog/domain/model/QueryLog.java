package com.posicube.assignment.querylog.domain.model;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.users.domain.model.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryLog {
    final private Long id;
    final private Long userId;
    final private ModelType type;
    final private String content;
    final private String answer;
    final private Long usedTokens;
    final private BigDecimal cost;
    final private LocalDateTime createAt;

    private static final int MAX_QUERY_LENGTH = 800;
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    public static QueryLog create(Users users, String q, ModelType type, String answer, Long usedTokens) {
        validationQueryLogInvariants(users, q, type, answer, usedTokens);
        BigDecimal cost = calculateCost(usedTokens, type);

        return QueryLog.builder()
                .userId(users.getId())
                .type(type)
                .content(q.trim())
                .answer(answer)
                .usedTokens(usedTokens)
                .cost(cost)
                .createAt(LocalDateTime.now())
                .build();
    }

    private static void validationQueryLogInvariants(Users user, String q, ModelType type, String answer, Long usedTokens) {
        if (Objects.isNull(user)) throw new BaseException(QueryLogExceptionStatus.USER_CANNOT_BE_NULL);
        if (Objects.isNull(q) || q.isBlank()) throw new BaseException(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL);
        if (Objects.isNull(type)) throw new BaseException(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        if (Objects.isNull(answer)) throw new BaseException(QueryLogExceptionStatus.ANSWER_CANNOT_BE_NULL);
        if (Objects.isNull(usedTokens)) throw new BaseException(QueryLogExceptionStatus.USED_TOKEN_CANNOT_BE_NULL);
        if (q.trim().length() > MAX_QUERY_LENGTH) throw new BaseException(QueryLogExceptionStatus.QUERY_TOO_LONG);
    }

    private static BigDecimal calculateCost(long tokens, ModelType type) {
        BigDecimal pricePer1KToken = type.getPricePer1KToken();
        BigDecimal tokenBigDecimal = BigDecimal.valueOf(tokens);

        BigDecimal costBefoeRounding = tokenBigDecimal.divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(pricePer1KToken);
        return costBefoeRounding.setScale(2, RoundingMode.HALF_UP);
    }
}
