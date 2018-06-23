package ru.kuzmichev.SimpleBank.server.service.accountowner;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleAccount {
    private Long id;
    private String number;
    private boolean enable;

    public SimpleAccount() {
        this.enable = true;
    }
}
