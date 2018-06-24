package ru.kuzmichev.SimpleBank.web.api.info.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.util.OwnerType;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AccountOwner {
    private Long id;
    private String fullName;
    private OwnerType type;
    private Date createdDate;
    private boolean enable;
}
