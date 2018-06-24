package ru.kuzmichev.SimpleBank.web.api.info.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.util.TerminalType;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Terminal {
    private Long id;
    private String address;
    private TerminalType type;
    private Date createdDate;
    private boolean enable;
    private Long accountId;
}
