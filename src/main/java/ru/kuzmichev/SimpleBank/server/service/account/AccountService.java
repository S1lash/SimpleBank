package ru.kuzmichev.SimpleBank.server.service.account;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountRepository;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Nullable
    @Transactional(readOnly = true)
    public Account getAvailableTerminalByNumber(String number) {
        AccountEntity accountEntity = accountRepository.findAccountEntityByNumber(number);
        if (accountEntity == null || !accountEntity.isEnable()) {
            return null;
        }
        return convert(accountEntity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAccount(Account account) {
        Assert.notNull(account, "account is null");
        Assert.notNull(account.getOwner(), "account.owner is null");

        accountRepository.save(convert(account));
    }
}
