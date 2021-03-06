package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.web.api.info.dto.Transaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class TransactionResponse extends SuccessfulResponse {
    private List<Transaction> transactions;
}
