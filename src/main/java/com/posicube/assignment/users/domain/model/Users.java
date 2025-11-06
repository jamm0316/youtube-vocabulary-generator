package com.posicube.assignment.users.domain.model;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.users.exception.TokenExceptionStatus;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    private final Long id;
    private final PlanType planType;
    private final String account;
    private final String password;
    private final String name;
    private final Tokens tokens;

    static public Users create(String account, String password, String name, Plan plan) {
        Tokens tokens = Tokens.initialOf(plan);
        validateUserInvariants(account, password, name, tokens);
        return Users.builder()
                .planType(plan.getType())
                .account(account)
                .password(password)
                .name(name)
                .tokens(tokens)
                .build();
    }

    private static void validateUserInvariants(String account, String password, String name, Tokens tokens) {
        if (account == null || account.trim().isEmpty()) throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL);
        if (password == null || password.trim().isEmpty()) throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL);
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

    public void useTokens(long amountToUse) {
        this.tokens.deduct(amountToUse);
    }
}
