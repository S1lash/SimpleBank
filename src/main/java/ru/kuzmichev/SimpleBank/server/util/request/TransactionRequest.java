package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.ToString;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

@ToString
public abstract class TransactionRequest {
    private Long amount;
    private TransactionType transactionType;
    private AccountInfo creditPart;
    private AccountInfo debitPart;

    public TransactionRequest (TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
