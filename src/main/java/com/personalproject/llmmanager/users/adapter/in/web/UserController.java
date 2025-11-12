package com.personalproject.llmmanager.users.adapter.in.web;

import com.personalproject.llmmanager.users.application.commandquery.UsageResponse;
import com.personalproject.llmmanager.users.application.commandquery.UserCreateRequest;
import com.personalproject.llmmanager.users.application.commandquery.UserInfoResponse;
import com.personalproject.llmmanager.users.application.service.UsersService;
import com.personalproject.llmmanager.users.domain.model.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoResponse create(@Valid @RequestBody UserCreateRequest request) {
        Users user = usersService.createUser(request);
        return UserInfoResponse.from(user);
    }

    @PostMapping("/usage")
    public UsageResponse getUsage(@RequestHeader("X-User-Id") Long userId) {
        return usersService.getUserUsage(userId);
    }
}
