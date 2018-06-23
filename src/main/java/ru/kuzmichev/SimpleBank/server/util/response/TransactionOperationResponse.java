package ru.kuzmichev.SimpleBank.server.util.response;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

import javax.persistence.Access;

@Data
@ToString
@Accessors(chain = true)
public class TransactionOperationResponse {
    @Nullable
    private Long transactionId;
    @Nullable
    private TransactionState transactionState;
    private TransactionType transactionType;
    private boolean error;
    @Nullable
    private String errorMessage;
}
