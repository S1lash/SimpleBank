package ru.kuzmichev.SimpleBank.server.service.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
