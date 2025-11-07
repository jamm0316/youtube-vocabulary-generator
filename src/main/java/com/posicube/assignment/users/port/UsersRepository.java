package com.posicube.assignment.users.port;

import com.posicube.assignment.users.application.commandquery.UserInfoResponse;
import com.posicube.assignment.users.domain.model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
    int resetAllUserTokens();
    List<UserInfoResponse> findAllUsersInfo();
    Optional<Users> findUsersByAccount(String account);
}
