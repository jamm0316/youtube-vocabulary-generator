package com.posicube.assignment.users.presentation;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.users.application.UserService;
import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.presentation.dtos.UserCreateRequest;
import com.posicube.assignment.users.presentation.dtos.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public BaseResponse<UserInfoResponse> create(@Valid @RequestBody UserCreateRequest request) {
        Users user = userService.createUser(request);
        return new BaseResponse<>(UserInfoResponse.from(user));
    }
}
