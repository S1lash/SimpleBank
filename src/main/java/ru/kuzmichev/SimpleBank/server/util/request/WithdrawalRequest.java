package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.Data;
import lombok.ToString;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

@Data
@ToString(callSuper = true)
public class WithdrawalRequest extends TransactionRequest {
    public WithdrawalRequest() {
        super(TransactionType.WITHDRAWAL);
    }
}
