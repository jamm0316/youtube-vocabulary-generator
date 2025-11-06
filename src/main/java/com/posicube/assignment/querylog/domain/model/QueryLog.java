package com.posicube.assignment.querylog.domain.model;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.querylog.exception.QueryLogExceptionStatus;
import com.posicube.assignment.users.domain.model.Users;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class QueryLog {
    private final Long id;
    private final Long userId;
    private final ModelType type;
    private final String content;
    private final String answer;
    private final Long usedTokens;
    private final LocalDateTime createAt;

    private static final int MAX_QUERY_LENGTH = 800;
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    //1. 생성 전용 메서드: Service 계층에서 새로운 QueryLog 만들때 사용
    public static QueryLog create(Users users, String q, ModelType type, String answer, Long usedTokens) {
        validationQueryLogInvariants(users, q, type, answer, usedTokens);

        return new QueryLog(null, users.getId(), type, q.trim(), answer, usedTokens, LocalDateTime.now());
    }

    //2. 재구성 전용 builder: JPA Entity -> Domain 변환 시 사용
    @Builder(builderMethodName = "fromPersistenceBuilder")
    private QueryLog (Long id, Long userId, ModelType type, String content, String answer, Long usedTokens, LocalDateTime createAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.answer = answer;
        this.usedTokens = usedTokens;
        this.createAt = createAt;
    }


    private static void validationQueryLogInvariants(Users user, String q, ModelType type, String answer, Long usedTokens) {
        if (Objects.isNull(user)) throw new BaseException(QueryLogExceptionStatus.USER_CANNOT_BE_NULL);
        if (Objects.isNull(q) || q.isBlank()) throw new BaseException(QueryLogExceptionStatus.QUERY_CANNOT_BE_NULL);
        if (Objects.isNull(type)) throw new BaseException(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        if (Objects.isNull(answer)) throw new BaseException(QueryLogExceptionStatus.ANSWER_CANNOT_BE_NULL);
        if (Objects.isNull(usedTokens)) throw new BaseException(QueryLogExceptionStatus.USED_TOKEN_CANNOT_BE_NULL);
        if (q.trim().length() > MAX_QUERY_LENGTH) throw new BaseException(QueryLogExceptionStatus.QUERY_TOO_LONG);
    }
}
