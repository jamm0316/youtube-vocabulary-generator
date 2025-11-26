package com.personalproject.llmmanager.users.adapter.in.web;

import com.personalproject.llmmanager.users.adapter.in.web.request.UserCreateRequest;
import com.personalproject.llmmanager.users.adapter.in.web.response.UsageResponse;
import com.personalproject.llmmanager.users.adapter.in.web.response.UserInfoResponse;
import com.personalproject.llmmanager.users.application.service.UsersService;
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
        return UserInfoResponse.from(usersService.createUser(request.toCommand()));
    }

    @PostMapping("/usage")
    public UsageResponse getUsage(@RequestHeader("X-User-Id") Long userId) {
        return UsageResponse.from(usersService.getUserUsage(userId));
    }
}
