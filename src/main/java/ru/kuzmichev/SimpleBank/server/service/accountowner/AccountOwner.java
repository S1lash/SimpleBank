package ru.kuzmichev.SimpleBank.server.service.accountowner;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.util.OwnerType;

import java.util.Date;
import java.util.Set;

@Data
@Accessors(chain = true)
public class AccountOwner {
    private Long id;
    private String fullName;
    private OwnerType type;
    private Date createdDate;
    private boolean enable;
    private Set<SimpleAccount> accounts;

    public AccountOwner() {
        this.createdDate = new Date();
        this.enable = true;
    }
}
