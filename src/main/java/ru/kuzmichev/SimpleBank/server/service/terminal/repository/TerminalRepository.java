package ru.kuzmichev.SimpleBank.server.service.terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<TerminalEntity, Long> {
}
