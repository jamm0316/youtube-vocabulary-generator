package com.posicube.assignment.plan.domain.model;

import com.posicube.assignment.common.exception.BaseException;
import com.posicube.assignment.plan.exception.PlanExceptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
    private PlanType type;  //사용자 요금제
    private LocalDateTime updateAt;

    static public Plan create(PlanType type) {
        validatePlanInvariants(type);
        return Plan.builder()
                .type(type)
                .updateAt(LocalDateTime.now())
                .build();
    }

    static private void validatePlanInvariants(PlanType type) {
        if (Objects.isNull(type)) {
            throw new BaseException(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL);
        }
    }



    public void changePlanType(PlanType newType) {
        if (Objects.isNull(newType)) {
            throw new BaseException(PlanExceptionStatus.PLAN_TYPE_CANNOT_BE_NULL);
        }

        if (this.type.equals(newType)) {
            throw new BaseException(PlanExceptionStatus.CANNOT_CHANGE_SAME_TYPE);
        }

        this.type = newType;
        this.updateAt = LocalDateTime.now();
    }
}
