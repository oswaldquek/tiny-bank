package com.tinybank.main.resource;

import com.tinybank.main.model.AccountInformationResponse;
import com.tinybank.main.model.Links;
import com.tinybank.main.model.TransactionHistoryResponse;
import com.tinybank.main.model.TransactionRequest;
import com.tinybank.main.model.UserEntity;
import com.tinybank.main.service.AccountsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping(path = "/v1/user/{userId}/account/add-transaction", produces = "application/json", consumes = "application/json")
    public ResponseEntity addTransaction(@PathVariable("userId") UserEntity user, @RequestBody TransactionRequest transactionRequest) {
        if (transactionRequest.amount() < 0 &&
                (accountsService.getAccountInformation(user.getId()).balance() - Math.abs(transactionRequest.amount()) < 0)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        accountsService.addTransaction(user.getId(), transactionRequest);
        AccountInformationResponse accountInformation = accountsService.getAccountInformation(user.getId());
        return ResponseEntity.ok().body(accountInformation);
    }

    @GetMapping(path = "/v1/user/{userId}/account", produces = "application/json")
    public ResponseEntity getAccount(@PathVariable("userId") UserEntity user) {
        AccountInformationResponse accountInformation = accountsService.getAccountInformation(user.getId());
        return ResponseEntity.ok().body(accountInformation);
    }

    @GetMapping(path = "/v1/user/{userId}/transaction-history", produces = "application/json")
    public TransactionHistoryResponse getTransactionHistory(@PathVariable("userId") UserEntity user, HttpServletRequest request) {
        var transactionHistory = accountsService.getTransactionHistory(user.getId());
        Links links = new Links(null, null, request.getRequestURL().append("?page=1&size=10").toString());
        return new TransactionHistoryResponse(transactionHistory, links);
    }
}
