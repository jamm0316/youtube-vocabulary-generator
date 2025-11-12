package com.personalproject.llmmanager.plan.adapter.out.persistence;

import com.personalproject.llmmanager.plan.domain.model.PlanType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
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
