package com.posicube.assignment.users.domain.entity;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.users.domain.vo.Tokens;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_type", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PLAN"))
    private Plan plan;

    @Column(nullable = false, length = 100)
    private String account;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Tokens tokens;

    private Users(String account, String password, String name, Plan plan) {
        this.plan = plan;
        Tokens tokens = Tokens.initialOf(plan);
        validateUserInvariants(account, password, name, tokens);
        this.account = account.trim();
        this.password = password.trim();
        this.name = name.trim();
        this.tokens = tokens;
    }

    private void validateUserInvariants(String account, String password, String name, Tokens tokens) {
        if (account == null || account.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL);
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL);
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.NAME_CANNOT_BE_NULL);
        }

        if (account.contains(" ")) {
            throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_CONTAIN_WHITESPACE);
        }

        if (password.contains(" ")) {
            throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_CONTAIN_WHITESPACE);
        }

        if (name.contains(" ")) {
            throw new BaseException(UserExceptionStatus.NAME_CANNOT_CONTAIN_WHITESPACE);
        }

        if (Objects.isNull(tokens)) {
            throw new BaseException(UserExceptionStatus.TOKEN_CANNOT_NULL);
        }
    }

    static public Users create(String account, String password, String name, Plan plan) {
        return new Users(account, password, name, plan);
    }
}
