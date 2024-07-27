package com.tinybank.main.service;

import com.tinybank.main.model.TransactionRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class TransferService {

    private final AccountsService accountsService;

    public TransferService(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    public void makeTransfer(String fromUserId, String toUserId, TransactionRequest transactionRequest) {
        if (accountsService.getAccountInformation(fromUserId).balance() - Math.abs(transactionRequest.amount()) < 0) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, format("User with id %s does not have enough money to transfer.", fromUserId));
        }
        accountsService.addTransaction(fromUserId, new TransactionRequest(transactionRequest.amount()*-1));
        accountsService.addTransaction(toUserId, transactionRequest);
    }
}
