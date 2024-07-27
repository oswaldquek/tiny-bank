package com.tinybank.main.converters;

import com.tinybank.main.dao.UsersRepository;
import com.tinybank.main.model.Status;
import com.tinybank.main.model.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class StringToUserConverter implements Converter<String, UserEntity> {

    private final UsersRepository usersRepository;

    public StringToUserConverter(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserEntity convert(String id) {
        UserEntity user = usersRepository.get(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with id %s not found", id)));

        if (user.getStatus().equals(Status.disabled)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, format("User with id %s has been disabled", id));
        }
        return user;
    }
}
