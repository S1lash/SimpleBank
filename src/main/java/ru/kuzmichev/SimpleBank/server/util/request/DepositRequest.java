package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.Data;
import lombok.ToString;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

@Data
@ToString(callSuper = true)
public class DepositRequest extends TransactionRequest {
    public DepositRequest() {
        super(TransactionType.DEPOSIT);
    }
}
