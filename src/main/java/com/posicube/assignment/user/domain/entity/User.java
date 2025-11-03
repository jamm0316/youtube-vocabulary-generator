package com.posicube.assignment.user.domain.entity;

import com.posicube.assignment.plan.domain.entity.Plan;
import jakarta.persistence.*;

@Entity
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
}
