package com.tinybank.main.users.resource;

import com.tinybank.main.users.model.AccountInformationResponse;
import com.tinybank.main.users.model.TransactionRequest;
import com.tinybank.main.users.service.AccountsService;
import com.tinybank.main.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class AccountsController {

    private final AccountsService accountsService;
    private final UsersService usersService;

    @Autowired
    public AccountsController(AccountsService accountsService, UsersService usersService) {
        this.accountsService = accountsService;
        this.usersService = usersService;
    }

    @PostMapping(path = "/v1/user/{userId}/account/add-transaction", produces = "application/json", consumes = "application/json")
    public ResponseEntity addTransaction(@PathVariable("userId") String userId, @RequestBody TransactionRequest transactionRequest) {
        if (usersService.getUser(userId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("User with id %s not found", userId));
        }
        if (transactionRequest.amount() < 0 &&
                (accountsService.getAccountInformation(userId).balance() - Math.abs(transactionRequest.amount()) < 0)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        accountsService.addTransaction(userId, transactionRequest);
        AccountInformationResponse accountInformation = accountsService.getAccountInformation(userId);
        return ResponseEntity.ok().body(accountInformation);
    }

    @GetMapping(path = "/v1/user/{userId}/account", produces = "application/json")
    public ResponseEntity getAccount(@PathVariable("userId") String userId) {
        AccountInformationResponse accountInformation = accountsService.getAccountInformation(userId);
        return ResponseEntity.ok().body(accountInformation);
    }
}
