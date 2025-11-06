package com.posicube.assignment.users.adapter.out.persistence;

import com.posicube.assignment.users.application.commandquery.UserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaUsersRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findUsersById(Long id);

    @Modifying
    @Query("UPDATE UsersJpaEntity u SET u.usedTokens = 0, u.remainingTokens = u.quota")
    int resetAllUserTokens();

    @Query("""
           SELECT new com.posicube.assignment.users.application.commandquery.UserInfoResponse(
              u.account,
              u.name,
              u.plan.type,
              new com.posicube.assignment.users.domain.model.Tokens(
                  u.quota,
                  u.usedTokens,
                  u.remainingTokens
              )
           )
           FROM UsersJpaEntity u
           """)
    List<UserInfoResponse> findAllUsersInfo();
}
