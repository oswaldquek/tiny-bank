package com.tinybank.main.users.dao;

import com.tinybank.main.users.model.TransactionEventEntity;

import java.util.List;

public interface AccountsRepository {
    void addTransaction(String userId, TransactionEventEntity transactionEventEntity);

    List<TransactionEventEntity> getTransactionEvents(String userId);
}
