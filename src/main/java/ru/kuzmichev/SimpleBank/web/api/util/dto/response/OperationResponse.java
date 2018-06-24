package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class OperationResponse extends SuccessfulResponse {
    private boolean error;
    private Long transactionId;
    private TransactionType transactionType;
    private TransactionState state;
    private String description;
}
