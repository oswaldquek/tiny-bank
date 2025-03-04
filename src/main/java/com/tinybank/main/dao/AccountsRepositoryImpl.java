package com.tinybank.main.dao;

import com.tinybank.main.model.TransactionEventEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class AccountsRepositoryImpl implements AccountsRepository {

    private static final Map<String, LinkedList<TransactionEventEntity>> transactions = new HashMap<>();

    @Override
    public void addTransaction(String userId, TransactionEventEntity transactionEventEntity) {
        transactions.computeIfAbsent(userId, k -> new LinkedList<>()).add(transactionEventEntity);
    }

    @Override
    public List<TransactionEventEntity> getTransactionEvents(String userId) {
        return transactions.getOrDefault(userId, new LinkedList<>());
    }
}
