package ru.kuzmichev.SimpleBank.server.service.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findAccountEntityByNumberAndEnable(String number, boolean enable);
    Set<AccountEntity> findAllByNumberInAndEnable(List<String> numbers, boolean enable);
}
