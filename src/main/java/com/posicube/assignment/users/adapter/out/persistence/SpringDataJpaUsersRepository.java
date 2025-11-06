package com.posicube.assignment.users.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataJpaUsersRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findUsersById(Long id);

    @Modifying
    @Query("UPDATE UsersJpaEntity u SET u.usedTokens = 0, u.remainingTokens = u.quota")
    int resetAllUserTokens();
}
