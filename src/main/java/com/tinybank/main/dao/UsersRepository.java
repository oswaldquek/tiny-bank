package com.tinybank.main.dao;

import com.tinybank.main.model.UserEntity;

import java.util.Optional;

public interface UsersRepository {

    void add(UserEntity userEntity);

    Optional<UserEntity> get(String id);
}
