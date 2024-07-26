package com.tinybank.main.users.resource;

import com.tinybank.main.users.model.CreateUserRequest;
import com.tinybank.main.users.model.UserResponse;
import com.tinybank.main.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/v1/user/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        UserResponse userResponse = usersService.getUser(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
