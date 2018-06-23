package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.util.AccountInfo;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
public class TransactionRequest {
    private Long amount;
    private TransactionType transactionType;
    private AccountInfo creditPart;
    private AccountInfo debitPart;

    public TransactionRequest (TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
