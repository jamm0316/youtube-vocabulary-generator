package com.personalproject.llmmanager.query.domain;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.query.exception.QueryExceptionStatus;
import com.personalproject.llmmanager.querylog.domain.model.ModelType;
import com.personalproject.llmmanager.querylog.domain.model.QueryLog;
import com.personalproject.llmmanager.querylog.domain.policy.TokenCalculator;
import com.personalproject.llmmanager.users.domain.model.Users;
import lombok.Getter;

import java.util.Objects;

/**
 * Query 애그리게잇 루트
 * Users와 QueryLog의 협력을 조율하는 중심 도메인
 */
@Getter
public class Query {
    private final Users user;
    private final String url;
    private final ModelType modelType;
    private Long usedTokens;
    private String answer;
    private QueryStatus status;

    private Query(Users user, String url, ModelType modelType, Long usedTokens, String answer, QueryStatus status) {
        this.user = user;
        this.url = url;
        this.modelType = modelType;
        this.usedTokens = usedTokens;
        this.answer = answer;
        this.status = status;
    }

    public static Query start(Users user, String url, ModelType modelType) {
        validationQueryInvariants(user, url, modelType);
        user.validateQueryPermission();
        return new Query(user, url, modelType, null, null, QueryStatus.PENDING);
    }

    private static void validationQueryInvariants(Users user, String url, ModelType modelType) {
        if (Objects.isNull(user)) {
            throw new BaseException(QueryExceptionStatus.USER_CANNOT_BE_NULL);
        }

        if (Objects.isNull(url) || url.isBlank()) {
            throw new BaseException(QueryExceptionStatus.URL_CANNOT_BE_NULL_OR_BLANK);
        }

        if (Objects.isNull(modelType)) {
            throw new BaseException(QueryExceptionStatus.MODEL_TYPE_CANNOT_BE_NULL);
        }
    }

    public Query calculateTokens(TokenCalculator calculator) {
        if (status != QueryStatus.PENDING) {
            throw new BaseException(QueryExceptionStatus.TOKEN_CALCULATION_IS_ONLY_POSSIBLE_IN_PENDING);
        }
        this.usedTokens = calculator.calculateTokensFromPrompt(this.url);
        return this;
    }

    public Query complete(String answer) {
        if (this.usedTokens == null) {
            throw new BaseException(QueryExceptionStatus.TOKEN_CALCULATION_IS_REQUIRED);
        }

        this.answer = answer;
        this.status = QueryStatus.COMPLETED;
        return this;
    }

    public QueryExecution execute() {
        if (this.status != QueryStatus.COMPLETED) {
            throw new BaseException(QueryExceptionStatus.QUERY_IS_NOT_COMPLETE);
        }
        Users updateUsers = user.useTokens(usedTokens);
        QueryLog queryLog = QueryLog.from(this, updateUsers);
        return new QueryExecution(updateUsers, queryLog);
    }
}
