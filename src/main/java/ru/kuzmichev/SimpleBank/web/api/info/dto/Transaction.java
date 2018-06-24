package ru.kuzmichev.SimpleBank.web.api.info.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Transaction {
    private Long id;
    private TransactionState state;
    private long amount;
    private Date createdDate;
    private Long debitAccountId;
    private Long creditAccountId;
    private TransactionType type;
}
