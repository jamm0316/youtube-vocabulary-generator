package com.posicube.assignment.querylog.domain;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelType type;

    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private long usedTokens;

    @Column(nullable = false)
    private double cost;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private QueryLog(User user, String q, ModelType type) {
        validateQueryLogInvariants(user, q, type);
        this.user = user;
        this.type = type;
        this.content = q.trim();
        this.usedTokens = calculateTokens(content);
        this.cost = calculateCost(this.usedTokens, type);
        createAt = LocalDateTime.now();
    }

    public static QueryLog create(User user, String q, ModelType type) {
        return new QueryLog(user, q, type);
    }

    private static void validateQueryLogInvariants(User user, String q, ModelType type) {
        if (Objects.isNull(user)) {
            throw new BaseException(QueryLogExceptionStatus.USER_CANNOT_BE_NULL);
        }

        if (Objects.isNull(q) || q.isBlank()) {
            throw new BaseException(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL);
        }

        if (Objects.isNull(type)) {
            throw new BaseException(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        }

        if (q.length() > 800) {
            throw new BaseException(QueryLogExceptionStatus.QUERY_TOO_LONG);
        }
    }

    private long calculateTokens(String q) {
        return Math.round(q.length() * 0.75);
    }

    private double calculateCost(long tokens, ModelType type) {
        double pricePer1KToken = type.getPricePer1KToken();
        double calculateCost = (tokens / 1_000.0) * pricePer1KToken;
        return Math.round(calculateCost * 100.0) / 100.0;
    }
}
