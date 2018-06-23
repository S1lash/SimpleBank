package ru.kuzmichev.SimpleBank.server.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class AccountInfo {
    @Nullable
    private Long clientId;
    @Nullable
    private String accountNumber;
    @Nullable
    private Long terminalId;
}
