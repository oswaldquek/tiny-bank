package com.tinybank.main.model;

import java.time.LocalDateTime;

public record TransactionEventEntity(long amount, LocalDateTime dateTime) {
    public static TransactionEventEntity from(TransactionRequest transactionRequest) {
        return new TransactionEventEntity(transactionRequest.amount(), LocalDateTime.now());
    }
}
