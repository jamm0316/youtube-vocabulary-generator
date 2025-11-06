package com.posicube.assignment.users.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaUsersRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findUsersById(Long id);

    @Query("""
            SELECT new com.posicube.assignment.users.adapter.out.persistence.UserTokenResetDto(
                u.id,
                u.plan.type,
                u.account,
                u.password,
                u.name,
                u.quota
               )
            FROM UsersJpaEntity u
            """)
    List<UserTokenResetDto> findAllFotTokenReset();
}
