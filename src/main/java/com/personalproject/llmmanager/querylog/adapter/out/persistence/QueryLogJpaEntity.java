package com.personalproject.llmmanager.querylog.adapter.out.persistence;

import com.personalproject.llmmanager.querylog.domain.model.ModelType;
import com.personalproject.llmmanager.users.adapter.out.persistence.UsersJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "query_log")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryLogJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_QUERY_LOG_USER"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsersJpaEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelType type;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private Long usedTokens;

    @Column(nullable = false)
    private LocalDateTime createAt;
}
