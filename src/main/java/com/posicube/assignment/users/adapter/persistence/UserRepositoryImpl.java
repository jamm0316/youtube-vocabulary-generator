package com.posicube.assignment.users.adapter.persistence;

import com.posicube.assignment.users.domain.entity.Users;
import com.posicube.assignment.users.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataJpaUserRepository jpa;

    @Override
    public Users save(Users users) {
        return jpa.save(users);
    }

    @Override
    public Optional<Users> findUserById(Long id) {
        return jpa.findUsersById(id);
    }
}
