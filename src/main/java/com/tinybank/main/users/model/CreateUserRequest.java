package com.tinybank.main.users.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CreateUserRequest(String firstName,
                                String lastName,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                Date dateOfBirth,
                                String address) {
}
