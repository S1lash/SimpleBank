package ru.kuzmichev.SimpleBank.server.service.accountowner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerRepository;

import java.util.List;

@Service
public class AccountOwnerService {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    public List<AccountOwnerEntity> getAll() {
        return accountOwnerRepository.findAll();
    }
}
