package ru.kuzmichev.SimpleBank.server.service.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwner;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id", "number"})
public class Account {
    private Long id;
    private long balance;
    private String pan;
    private Date createdDate;
    private String number;
    private boolean enable;
    private AccountOwner owner;

    public Account() {
        this.createdDate = new Date();
        this.enable = true;
        this.balance = 0L;
    }

    public void increaseBalance(long amount) {
        this.balance += amount;
    }

    public void decreaseBalance(long amount) {
        this.balance -= amount;
    }
}
