package ru.kuzmichev.SimpleBank.web.api.util.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
public class AccountInfo {
    @Nullable
    private Long clientId;
    @Nullable
    private String accountNumber;
    @Nullable
    private Long terminalId;
}
