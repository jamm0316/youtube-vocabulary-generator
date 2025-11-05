package com.posicube.assignment.users.domain.port;

import com.posicube.assignment.users.domain.entity.Users;

import java.util.Optional;

public interface UserRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
}
