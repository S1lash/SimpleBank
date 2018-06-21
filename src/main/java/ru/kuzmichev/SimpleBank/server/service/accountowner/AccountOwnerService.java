package ru.kuzmichev.SimpleBank.server.service.accountowner;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerRepository;

@Service
public class AccountOwnerService {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    @Nullable
    @Transactional(readOnly = true)
    public AccountOwner getAvailableTerminalById(long id) {
        AccountOwnerEntity accountOwnerEntity = accountOwnerRepository.getOne(id);
        if (accountOwnerEntity == null || !accountOwnerEntity.isEnable()) {
            return null;
        }
        return convert(accountOwnerEntity);
    }

    @Nullable
    private AccountOwner convert(AccountOwnerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AccountOwner()
                .setId(entity.getId())
                .setFullName(entity.getFullName())
                .setAccounts(entity.getAccounts())
                .setCreatedDate(entity.getCreatedDate())
                .setEnable(entity.isEnable())
                .setType(entity.getType());
    }
}
