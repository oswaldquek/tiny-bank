package com.tinybank.main.users.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserResponse(String id,
                           Status status,
                           String firstName,
                           String lastName,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                 Date dateOfBirth,
                           String address) {
    public static UserResponse from(UserEntity userEntity) {
        return new UserResponse(userEntity.getId(), userEntity.getStatus(), userEntity.getFirstName(),
                userEntity.getLastName(), userEntity.getDateOfBirth(), userEntity.getAddress());
    }
}
