package com.personalproject.llmmanager.plan.domain.model;

import com.personalproject.llmmanager.common.exception.BaseException;
import com.personalproject.llmmanager.plan.exception.PlanExceptionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Plan {
    private PlanType type;  //사용자 요금제
    private LocalDateTime updateAt;

    //1. 생성 전용 메서드: Service 계층에서 새로운 Plan 만들때 사용
    static public Plan create(PlanType type) {
        validatePlanInvariants(type);
        return new Plan(type, LocalDateTime.now());

    }

    //2. 재구성 전용 builder: JPA Entity -> Domain 변환 시 사용
    @Builder(builderMethodName = "fromPersistenceBuilder")
    private Plan(PlanType type, LocalDateTime updateAt) {
        this.type = type;
        this.updateAt = updateAt;
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
