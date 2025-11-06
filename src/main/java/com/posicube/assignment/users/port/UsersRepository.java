package com.posicube.assignment.users.port;

import com.posicube.assignment.users.adapter.out.persistence.UserTokenResetDto;
import com.posicube.assignment.users.domain.model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
    List<UserTokenResetDto> findAllFotTokenReset();
}
