package com.posicube.assignment.plan.adapter.out.persistence;

import com.posicube.assignment.plan.domain.model.PlanType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanJpaEntity {
    @Id
    @Enumerated(EnumType.STRING)
    private PlanType type;
    private LocalDateTime updateAt;

}
