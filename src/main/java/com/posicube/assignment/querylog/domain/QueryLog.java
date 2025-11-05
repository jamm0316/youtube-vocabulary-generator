package com.posicube.assignment.querylog.domain;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.domain.vo.ModelType;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.users.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUERY_LOG_USER"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelType type;

    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private Long usedTokens;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal cost;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private static final int MAX_QUERY_LENGTH = 800;
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    private QueryLog(Users user, String q, ModelType type, String answer, Long usedTokens) {
        validateQueryLogInvariants(user, q, type, answer, usedTokens);
        this.user = user;
        this.type = type;
        this.content = q.trim();
        this.answer = answer;
        this.usedTokens = usedTokens;
        this.cost = calculateCost(this.usedTokens, type);
        createAt = LocalDateTime.now();
    }

    public static QueryLog create(Users user, String q, ModelType type, String answer, Long usedTokens) {
        return new QueryLog(user, q, type, answer, usedTokens);
    }

    private static void validateQueryLogInvariants(Users user, String q, ModelType type, String answer, Long usedTokens) {
        if (Objects.isNull(user)) {
            throw new BaseException(QueryLogExceptionStatus.USER_CANNOT_BE_NULL);
        }

        if (Objects.isNull(q) || q.isBlank()) {
            throw new BaseException(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL);
        }

        if (Objects.isNull(type)) {
            throw new BaseException(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        }

        if (Objects.isNull(answer)) {
            throw new BaseException(QueryLogExceptionStatus.ANSWER_CANNOT_BE_NULL);
        }

        if (Objects.isNull(usedTokens)) {
            throw new BaseException(QueryLogExceptionStatus.USED_TOKEN_CANNOT_BE_NULL);
        }

        if (q.trim().length() > MAX_QUERY_LENGTH) {
            throw new BaseException(QueryLogExceptionStatus.QUERY_TOO_LONG);
        }
    }

    private BigDecimal calculateCost(long tokens, ModelType type) {
        BigDecimal pricePer1KToken = type.getPricePer1KToken();
        BigDecimal tokenBigDecimal = BigDecimal.valueOf(tokens);

        BigDecimal costBefoeRounding = tokenBigDecimal.divide(THOUSAND, 10, RoundingMode.HALF_UP)
                .multiply(pricePer1KToken);
        return costBefoeRounding.setScale(2, RoundingMode.HALF_UP);
    }
}
