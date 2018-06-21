package ru.kuzmichev.SimpleBank.server.service.transaction;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Transaction {
    private long id;
    private TransactionState state;
    private long amount;
    private Date createdDate;
    private Account debitAccount;
    private Account creditAccount;
    private TransactionType type;
}
