package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.plan.adapter.out.persistence.PlanJpaEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersJpaEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_type", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PLAN"))
    private PlanJpaEntity plan;

    @Column(nullable = false, length = 100, unique = true)
    private String account;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private long quota;

    @Column(nullable = false)
    private long usedTokens;

    @Column(nullable = false)
    private long remainingTokens;

    @Version
    private Long version;
}
