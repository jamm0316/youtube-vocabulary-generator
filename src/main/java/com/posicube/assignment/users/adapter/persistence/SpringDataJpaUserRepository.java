package com.posicube.assignment.users.adapter.persistence;

import com.posicube.assignment.users.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersById(Long id);
}
