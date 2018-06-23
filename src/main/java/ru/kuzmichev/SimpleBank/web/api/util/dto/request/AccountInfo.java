package ru.kuzmichev.SimpleBank.web.api.util.dto.request;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class AccountInfo {
    @Nullable
    private Long clientId;
    @Nullable
    private String accountNumber;
    @Nullable
    private Long terminalId;
}
