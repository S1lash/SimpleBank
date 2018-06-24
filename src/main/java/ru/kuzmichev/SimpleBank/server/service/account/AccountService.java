package ru.kuzmichev.SimpleBank.server.service.account;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Nullable
    @Transactional(readOnly = true)
    public Account getAvailableAccountByNumber(String number) {
        log.debug("Get available account with number [{}]", number);
        AccountEntity accountEntity = accountRepository.findAccountEntityByNumberAndEnable(number, true);
        if (accountEntity == null) {
            return null;
        }
        return convert(accountEntity);
    }

    @Transactional(readOnly = true)
    public Set<Account> getAvailableAccountsByNumbers(List<String> numbers) {
        log.debug("Get available accounts with numbers [{}]", numbers);
        Set<AccountEntity> accountEntities = accountRepository.findAllByNumberInAndEnable(numbers, true);
        if (accountEntities == null || accountEntities.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return accountEntities.stream()
                .map(a -> convert(a))
                .collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAccount(Account account) {
        log.debug("Save account [{}]", account);
        Assert.notNull(account, "account is null");

        long accountId = accountRepository.save(convert(account)).getId();
        account.setId(accountId);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllByIds(List<Long> ids) {
        log.debug("Get all accounts with ids [{}]", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return accountRepository.findAll().stream()
                    .map(a -> convert(a))
                    .collect(Collectors.toList());
        }
        return accountRepository.findAllById(ids).stream()
                .map(a -> convert(a))
                .collect(Collectors.toList());
    }
}
