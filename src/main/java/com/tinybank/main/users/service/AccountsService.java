package com.tinybank.main.users.service;

import com.tinybank.main.users.dao.AccountsRepository;
import com.tinybank.main.users.model.AccountInformationResponse;
import com.tinybank.main.users.model.TransactionEventEntity;
import com.tinybank.main.users.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountsService {

    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void addTransaction(String userId, TransactionRequest transactionRequest) {
        accountsRepository.addTransaction(userId, TransactionEventEntity.from(transactionRequest));
    }

    public AccountInformationResponse getAccountInformation(String userId) {
        Long balance = accountsRepository.getTransactionEvents(userId)
                .stream().map(TransactionEventEntity::amount)
                .reduce(0L, Long::sum);
        return new AccountInformationResponse(userId, balance);
    }

    public List<TransactionEventEntity> getTransactionHistory(String userId) {
        return accountsRepository.getTransactionEvents(userId);
    }
}
