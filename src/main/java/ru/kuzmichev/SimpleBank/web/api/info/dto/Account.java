package ru.kuzmichev.SimpleBank.web.api.info.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Account {
    private Long id;
    private long balance;
    private String pan;
    private Date createdDate;
    private String number;
    private boolean enable;
    private Long ownerId;
}
