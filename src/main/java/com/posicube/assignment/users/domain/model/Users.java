package com.posicube.assignment.users.domain.model;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.users.exception.TokenExceptionStatus;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Users {
    private final Long id;
    private final PlanType planType;
    private final String account;
    private final String password;
    private final String name;
    private final Tokens tokens;
    private final Long version;

    //1. 생성 전용 메서드: Service 계층에서 새로운 Users 만들때 사용
    static public Users create(String account, String password, String name, Plan plan) {
        Tokens initialledToken = Tokens.initialOf(plan);
        validateUserInvariants(account, password, name, initialledToken);
        return new Users(null, plan.getType(), account, password, name, initialledToken, null);
    }

    //2. 재구성 전용 builder: JPA Entity -> Domain 변환 시 사용
    @Builder(builderMethodName = "fromPersistenceBuilder")
    private Users(Long id, PlanType planType, String account, String password, String name, Tokens tokens, Long version) {
        this.id = id;
        this.planType = planType;
        this.account = account;
        this.password = password;
        this.name = name;
        this.tokens = tokens;
        this.version = version;
    }

    private static void validateUserInvariants(String account, String password, String name, Tokens tokens) {
        if (account == null || account.trim().isEmpty())
            throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL);
        if (password == null || password.trim().isEmpty())
            throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL);
        if (name == null || name.trim().isEmpty()) throw new BaseException(UserExceptionStatus.NAME_CANNOT_BE_NULL);
        if (account.contains(" ")) throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_CONTAIN_WHITESPACE);
        if (password.contains(" ")) throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_CONTAIN_WHITESPACE);
        if (name.contains(" ")) throw new BaseException(UserExceptionStatus.NAME_CANNOT_CONTAIN_WHITESPACE);
        if (Objects.isNull(tokens)) throw new BaseException(UserExceptionStatus.TOKEN_CANNOT_NULL);
    }

    public void validateQueryPermission() {
        if (this.tokens.getRemainingTokens() <= 0) {
            throw new BaseException(TokenExceptionStatus.INSUFFICIENT_TOKENS);
        }
    }

    public Users useTokens(long amountToUse) {
        Tokens newTokens = this.tokens.deduct(amountToUse);
        return new Users(
                this.id,
                this.planType,
                this.account,
                this.password,
                this.name,
                newTokens,
                this.version
        );
    }
}
