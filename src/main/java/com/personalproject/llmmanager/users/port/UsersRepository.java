package com.personalproject.llmmanager.users.port;

import com.personalproject.llmmanager.users.application.commandquery.UserInfoResponse;
import com.personalproject.llmmanager.users.domain.model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
    int resetAllUserTokens();
    List<UserInfoResponse> findAllUsersInfo();
    Optional<Users> findUsersByAccount(String account);
}
