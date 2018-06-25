package ru.kuzmichev.SimpleBank.server.service.accountowner;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Slf4j
@Service
public class AccountOwnerService {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    @Nullable
    @Transactional(readOnly = true)
    public AccountOwner getAvailableAccountOwnerById(long id) {
        log.debug("Get available accountOwner with id [{}]", id);
        List<AccountOwner> owners = getAllByIds(Collections.singletonList(id));
        if (owners.isEmpty()) {
            return null;
        }
        AccountOwner owner = owners.get(0);
        if (!owner.isEnable()) {
            return null;
        }
        return owner;
    }

    @Transactional(readOnly = true)
    public List<AccountOwner> getAllByIds(List<Long> ids) {
        log.debug("Get all accountOwners with ids [{}]", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return accountOwnerRepository.findAll().stream()
                    .map(ao -> convert(ao))
                    .collect(Collectors.toList());
        }
        return accountOwnerRepository.findAllById(ids).stream()
                .map(ao -> convert(ao))
                .collect(Collectors.toList());
    }
}
