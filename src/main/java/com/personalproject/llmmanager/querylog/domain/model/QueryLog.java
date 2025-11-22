package com.personalproject.llmmanager.querylog.domain.model;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.querylog.exception.QueryLogExceptionStatus;
import com.personalproject.llmmanager.users.domain.model.Users;
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
    private final String url;
    private final String answer;
    private final Long usedTokens;
    private final LocalDateTime createAt;

    private static final int MAX_QUERY_LENGTH = 800;
    private static final BigDecimal THOUSAND = new BigDecimal("1000");

    //1. 생성 전용 메서드: Service 계층에서 새로운 QueryLog 만들때 사용
    public static QueryLog create(Users users, String url, ModelType type, String answer, Long usedTokens) {
        validationQueryLogInvariants(users, url, type, answer, usedTokens);

        return new QueryLog(null, users.getId(), type, url.trim(), answer, usedTokens, LocalDateTime.now());
    }

    //2. 재구성 전용 builder: JPA Entity -> Domain 변환 시 사용
    @Builder(builderMethodName = "fromPersistenceBuilder")
    private QueryLog (Long id, Long userId, ModelType type, String url, String answer, Long usedTokens, LocalDateTime createAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.url = url;
        this.answer = answer;
        this.usedTokens = usedTokens;
        this.createAt = createAt;
    }


    private static void validationQueryLogInvariants(Users user, String url, ModelType type, String answer, Long usedTokens) {
        if (Objects.isNull(user)) throw new BaseException(QueryLogExceptionStatus.USER_CANNOT_BE_NULL);
        if (Objects.isNull(url) || url.isBlank()) throw new BaseException(QueryLogExceptionStatus.URL_CANNOT_BE_NULL);
        if (Objects.isNull(type)) throw new BaseException(QueryLogExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        if (Objects.isNull(answer)) throw new BaseException(QueryLogExceptionStatus.ANSWER_CANNOT_BE_NULL);
        if (Objects.isNull(usedTokens)) throw new BaseException(QueryLogExceptionStatus.USED_TOKEN_CANNOT_BE_NULL);
    }
}
