package com.tinybank.main.users.dao;

import com.tinybank.main.users.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository {

    private static Map<String, UserEntity> users = new HashMap<>();

    public void add(UserEntity userEntity) {
        users.put(userEntity.getId(), userEntity);
    }

    @Override
    public Optional<UserEntity> get(String id) {
        return Optional.ofNullable(users.get(id));
    }
}
