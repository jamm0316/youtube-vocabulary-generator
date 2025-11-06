package com.posicube.assignment.users.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaUsersRepository extends JpaRepository<UsersJpaEntity, Long> {

    Optional<UsersJpaEntity> findUsersById(Long id);
}
