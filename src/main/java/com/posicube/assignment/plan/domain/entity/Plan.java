package com.posicube.assignment.plan.domain.entity;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.vo.PlanType;
import com.posicube.assignment.plan.domain.vo.Tokens;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {
    @Id
    @Enumerated(EnumType.STRING)
    private PlanType type;  //사용자 요금제

    @Embedded
    @Column(nullable = false)
    private Tokens tokens;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    private Plan(PlanType type) {
        validatePlanInvariants(type);
        Tokens tokens = Tokens.create(type);
        this.type = type;
        this.tokens = tokens;
        totalPrice = BigDecimal.ZERO;
        updateAt = LocalDateTime.now();
    }

    private void validatePlanInvariants(PlanType type) {
        if (Objects.isNull(type)) {
            throw new BaseException(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL);
        }
    }

    static public Plan create(PlanType type) {
        return new Plan(type);
    }

    public void changePlanType(PlanType newType) {
        if (Objects.isNull(newType)) {
            throw new BaseException(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL);
        }

        if (this.type.equals(newType)) {
            throw new BaseException(PlanExceptionStatus.CANNOT_CHANGE_SAME_TYPE);
        }

        this.type = newType;
        this.tokens = Tokens.create(newType);
        this.updateAt = LocalDateTime.now();
    }
}
