package com.posicube.assignment.users.domain.port;

import com.posicube.assignment.users.domain.entity.Users;

public interface UserRepository {
    Users save(Users users);
}
