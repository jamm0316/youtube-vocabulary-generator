package com.llmmanager.assignment.users.port;

import com.llmmanager.assignment.users.application.commandquery.UserInfoResponse;
import com.llmmanager.assignment.users.domain.model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
    int resetAllUserTokens();
    List<UserInfoResponse> findAllUsersInfo();
    Optional<Users> findUsersByAccount(String account);
}
