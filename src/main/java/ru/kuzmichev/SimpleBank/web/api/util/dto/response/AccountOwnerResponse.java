package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.web.api.info.dto.AccountOwner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountOwnerResponse extends SuccessfulResponse {
    private List<AccountOwner> accountOwners;
}
