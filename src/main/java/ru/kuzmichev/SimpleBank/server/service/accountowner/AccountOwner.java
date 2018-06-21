package ru.kuzmichev.SimpleBank.server.service.accountowner;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.OwnerType;

import java.util.Date;
import java.util.Set;

@Data
@Accessors(chain = true)
public class AccountOwner {
    private long id;
    private String fullName;
    private OwnerType type;
    private Date createdDate;
    private boolean enable;
    private Set<AccountEntity> accounts;
}
