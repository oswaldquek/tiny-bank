package com.tinybank.main.users.service;

import com.tinybank.main.users.dao.UsersRepository;
import com.tinybank.main.users.model.CreateUserRequest;
import com.tinybank.main.users.model.TransactionRequest;
import com.tinybank.main.users.model.UserResponse;
import com.tinybank.main.users.model.Status;
import com.tinybank.main.users.model.UserEntity;
import com.tinybank.main.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class UsersService {

    private final UsersRepository usersRepository;
    private final AccountsService accountsService;

    @Autowired
    public UsersService(UsersRepository usersRepository, AccountsService accountsService) {
        this.usersRepository = usersRepository;
        this.accountsService = accountsService;
    }

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        UserEntity userEntity = UserEntity.newUser(createUserRequest, RandomIdGenerator.newUserId(), Status.active);
        usersRepository.add(userEntity);
        accountsService.addTransaction(userEntity.getId(), new TransactionRequest(0L));
        return UserResponse.from(userEntity);
    }

    public void deactivateUser(String id) {
        usersRepository.get(id).ifPresentOrElse(userEntity -> userEntity.setStatus(Status.disabled),
                () -> {
                    throw new ResponseStatusException(NOT_FOUND, format("User with id %s not found", id));
                });
    }

    public Optional<UserResponse> getUser(String id) {
        return usersRepository.get(id).map(UserResponse::from);
    }
}
