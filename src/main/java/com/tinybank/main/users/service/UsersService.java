package com.tinybank.main.users.service;

import com.tinybank.main.users.dao.UsersRepository;
import com.tinybank.main.users.model.CreateUserRequest;
import com.tinybank.main.users.model.UserResponse;
import com.tinybank.main.users.model.Status;
import com.tinybank.main.users.model.UserEntity;
import com.tinybank.main.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        UserEntity userEntity = UserEntity.newUser(createUserRequest, RandomIdGenerator.newUserId(), Status.active);
        usersRepository.add(userEntity);
        return UserResponse.from(userEntity);
    }

    public void deactivateUser(String id) {
        usersRepository.get(id).ifPresentOrElse(userEntity -> userEntity.setStatus(Status.disabled),
                () -> {
                    throw new ResponseStatusException(NOT_FOUND, format("User with id %s not found", id));
                });
    }

    public UserResponse getUser(String id) {
        return usersRepository.get(id).map(UserResponse::from)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with id %s not found", id)));
    }
}
