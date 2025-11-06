package com.posicube.assignment.users.adapter.in.web;

import com.posicube.assignment.common.baseResponse.BaseResponse;
import com.posicube.assignment.users.application.service.UsersService;
import com.posicube.assignment.users.adapter.out.persistence.UsersJpaEntity;
import com.posicube.assignment.users.application.commandquery.UserCreateRequest;
import com.posicube.assignment.users.application.commandquery.UserInfoResponse;
import com.posicube.assignment.users.domain.model.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping("")
    public BaseResponse<UserInfoResponse> create(@Valid @RequestBody UserCreateRequest request) {
        Users user = usersService.createUser(request);
        return new BaseResponse<>(UserInfoResponse.from(user));
    }
}
