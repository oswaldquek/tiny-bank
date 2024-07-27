package com.tinybank.main.resource;

import com.tinybank.main.model.CreateUserRequest;
import com.tinybank.main.model.UserResponse;
import com.tinybank.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(path = "/v1/user", produces = "application/json", consumes = "application/json")
    public ResponseEntity createUser(@RequestBody CreateUserRequest user) {
        UserResponse userResponse = usersService.createUser(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/v1/user/{id}/deactivate")
    public ResponseEntity deactivateUser(@PathVariable String id) {
        usersService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/v1/user/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        return usersService.getUser(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with id %s not found", id)));
    }
}
