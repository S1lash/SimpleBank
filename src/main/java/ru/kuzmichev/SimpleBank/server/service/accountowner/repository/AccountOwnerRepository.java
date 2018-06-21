package ru.kuzmichev.SimpleBank.server.service.accountowner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOwnerRepository extends JpaRepository<AccountOwnerEntity, Long> {
}
