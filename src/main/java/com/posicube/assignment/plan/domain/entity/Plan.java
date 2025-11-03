package com.posicube.assignment.plan.domain.entity;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.domain.entity.vo.Tokens;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {
    @Id
    @Enumerated(EnumType.STRING)
    private PlanType type;  //사용자 요금제

    @Embedded
    @Column(nullable = false)
    private Tokens tokens;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    public Plan(int totalPrice) {
        validatePlanInvariants(totalPrice);
    }

    private void validatePlanInvariants(int totalPrice) {
        if (totalPrice < 0) {
            throw new BaseException(PlanExceptionStatus.INVALID_TOTAL_PRICE);
        }
    }
}
