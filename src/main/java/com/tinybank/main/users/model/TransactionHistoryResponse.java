package com.tinybank.main.users.model;

import java.util.List;

public record TransactionHistoryResponse(List<TransactionEventEntity> results,
                                         Links links) {
}
