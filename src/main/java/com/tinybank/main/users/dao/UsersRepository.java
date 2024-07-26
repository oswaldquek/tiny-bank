package com.tinybank.main.users.dao;

import com.tinybank.main.users.model.UserEntity;

import java.util.Optional;

public interface UsersRepository {

    void add(UserEntity userEntity);

    Optional<UserEntity> get(String id);
}
