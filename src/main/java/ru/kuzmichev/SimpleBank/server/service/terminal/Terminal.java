package ru.kuzmichev.SimpleBank.server.service.terminal;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.TerminalType;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Terminal {
    private long id;
    private String address;
    private TerminalType type;
    private Date createdDate;
    private boolean enable;
    private AccountEntity account;
}
