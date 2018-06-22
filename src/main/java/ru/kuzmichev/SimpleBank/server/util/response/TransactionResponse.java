package ru.kuzmichev.SimpleBank.server.util.response;

import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;

@Data
@ToString
public class TransactionResponse {
    @Nullable
    private Long transactionId;
    @Nullable
    private TransactionState transactionState;
    private boolean error;
    @Nullable
    private String errorMessage;
}
