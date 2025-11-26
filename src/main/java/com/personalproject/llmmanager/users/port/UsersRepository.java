package com.personalproject.llmmanager.users.port;

import com.personalproject.llmmanager.users.application.dtos.result.UserInfoResult;
import com.personalproject.llmmanager.users.domain.model.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);
    Optional<Users> findUserById(Long id);
    int resetAllUserTokens();
    List<UserInfoResult> findAllUsersInfo();
    Optional<Users> findUsersByAccount(String account);
}
