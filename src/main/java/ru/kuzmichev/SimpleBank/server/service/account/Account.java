package ru.kuzmichev.SimpleBank.server.service.account;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwner;
import ru.kuzmichev.SimpleBank.server.service.terminal.Terminal;
import ru.kuzmichev.SimpleBank.server.service.transaction.Transaction;

import java.util.Date;
import java.util.Set;

@Data
@Accessors(chain = true)
public class Account {
    private long id;
    private long balance;
    private String pan;
    private Date createdDate;
    private String number;
    private boolean enable;
    private AccountOwner owner;
    private Set<Transaction> debitTransactions;
    private Set<Transaction> creditTransactions;
    private Set<Terminal> terminals;
}