package com.tinybank.main.model;

import java.util.List;

public record TransactionHistoryResponse(List<TransactionEventEntity> results,
                                         Links links) {
}
