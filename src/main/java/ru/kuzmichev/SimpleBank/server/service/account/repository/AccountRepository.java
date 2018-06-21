package ru.kuzmichev.SimpleBank.server.service.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findAccountEntityByNumber(String number);
}
