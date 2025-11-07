package com.posicube.assignment.users.adapter.in.web;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.users.application.commandquery.UsageResponse;
import com.posicube.assignment.users.application.commandquery.UserCreateRequest;
import com.posicube.assignment.users.application.commandquery.UserInfoResponse;
import com.posicube.assignment.users.application.service.UsersService;
import com.posicube.assignment.users.domain.model.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping("/users")
    public ResponseEntity<BaseResponse<UserInfoResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        Users user = usersService.createUser(request);
        UserInfoResponse from = UserInfoResponse.from(user);
        return BaseResponse.success(from, HttpStatus.CREATED);
    }

    @PostMapping("/usage")
    public ResponseEntity<BaseResponse<UsageResponse>> getUsage(@RequestHeader("X-User-Id") Long userId) {
        return BaseResponse.success(usersService.getUserUsage(userId));
    }
}
