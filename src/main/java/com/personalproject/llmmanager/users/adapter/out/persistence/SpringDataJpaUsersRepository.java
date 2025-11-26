package com.personalproject.llmmanager.users.adapter.out.persistence;

import com.personalproject.llmmanager.users.application.dtos.result.UserInfoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaUsersRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findUsersById(Long id);

    Optional<UsersJpaEntity> findUsersByAccount(String account);


    @Modifying
    @Query("UPDATE UsersJpaEntity u SET u.usedTokens = 0, u.remainingTokens = u.quota")
    int resetAllUserTokens();

    @Query("""
           SELECT new com.personalproject.llmmanager.users.application.dtos.result.UserInfoResult(
              u.account,
              u.name,
              u.plan.type,
              new com.personalproject.llmmanager.users.domain.model.Tokens(
                  u.quota,
                  u.usedTokens,
                  u.remainingTokens
              )
           )
           FROM UsersJpaEntity u
           """)
    List<UserInfoResult> findAllUsersInfo();
}
