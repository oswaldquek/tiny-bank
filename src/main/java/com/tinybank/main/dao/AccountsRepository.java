package com.tinybank.main.dao;

import com.tinybank.main.model.TransactionEventEntity;

import java.util.List;

public interface AccountsRepository {
    void addTransaction(String userId, TransactionEventEntity transactionEventEntity);

    List<TransactionEventEntity> getTransactionEvents(String userId);
}
