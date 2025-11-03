package com.posicube.assignment.users.domain.entity;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.Plan;
import com.posicube.assignment.plan.domain.entity.PlanType;
import com.posicube.assignment.users.exception.UserExceptionStatus;
import com.posicube.assignment.users.presentation.dtos.UserCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
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

    private Users(String account, String password, String name, PlanType type) {
        plan = Plan.create(type);
        validateUserInvariants(account, password, name);
        this.account = account.trim();
        this.password = password.trim();
        this.name = name.trim();
    }

    private void validateUserInvariants(String account, String password, String name) {
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
    }

    static public Users create(String account, String password, String name, PlanType plan) {
        return new Users(account, password, name, plan);
    }
}
