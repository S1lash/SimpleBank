package ru.kuzmichev.SimpleBank.server.service.accountowner;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerRepository;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Service
public class AccountOwnerService {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    @Nullable
    @Transactional(readOnly = true)
    public AccountOwner getAvailableAccountOwnerById(long id) {
        AccountOwnerEntity accountOwnerEntity = accountOwnerRepository.getOne(id);
        if (accountOwnerEntity == null || !accountOwnerEntity.isEnable()) {
            return null;
        }
        return convert(accountOwnerEntity);
    }
}
