package ru.kuzmichev.SimpleBank.server.util.request;

import lombok.Data;
import lombok.ToString;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

@Data
@ToString(callSuper = true)
public class TransferRequest extends TransactionRequest {
    public TransferRequest() {
        super(TransactionType.TRANSFER);
    }
}
