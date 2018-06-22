package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@EqualsAndHashCode
@ToString
public class AccountInfo {
    @Nullable
    private Long clientId;
    @Nullable
    private String accountNumber;
    @Nullable
    private Long terminalId;
}
