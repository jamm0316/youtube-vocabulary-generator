package com.posicube.assignment.user.domain.entity;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.user.exception.UserExceptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "plan_type", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PLAN"))
    private Plan plan;

    @Column(nullable = false, length = 100)
    private String account;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    private User(String account, String password, String name) {
        validateUserInvariants(account, password, name);
        plan = Plan.createLite();
        this.account = account;
        this.password = password;
        this.name = name;
    }

    private void validateUserInvariants(String account, String password, String name) {
        if (account == null || account.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_BE_NULL);
        }

        if (account.contains(" ")) {
            throw new BaseException(UserExceptionStatus.ACCOUNT_CANNOT_CONTAIN_WHITESPACE);
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_BE_NULL);
        }

        if (password.contains(" ")) {
            throw new BaseException(UserExceptionStatus.PASSWORD_CANNOT_CONTAIN_WHITESPACE);
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(UserExceptionStatus.NAME_CANNOT_BE_NULL);
        }

        if (name.contains(" ")) {
            throw new BaseException(UserExceptionStatus.NAME_CANNOT_CONTAIN_WHITESPACE);
        }
    }

    public User create(String account, String password, String name) {
        return new User(account, password, name);
    }
}
