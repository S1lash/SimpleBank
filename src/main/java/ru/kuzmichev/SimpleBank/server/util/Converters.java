package ru.kuzmichev.SimpleBank.server.util;

import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwner;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.Terminal;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;

import java.util.stream.Collectors;

public class Converters {

    @Nullable
    public static Terminal convert(TerminalEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Terminal()
                .setId(entity.getId())
                .setAddress(entity.getAddress())
                .setAccount(convert(entity.getAccount()))
                .setCreatedDate(entity.getCreatedDate())
                .setEnable(entity.isEnable())
                .setType(entity.getType());
    }

    @Nullable
    public static AccountOwner convert(AccountOwnerEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AccountOwner()
                .setId(entity.getId())
                .setFullName(entity.getFullName())
                .setAccounts(entity.getAccounts().stream()
                    .map(a -> a.getId())
                    .collect(Collectors.toSet()))
                .setCreatedDate(entity.getCreatedDate())
                .setEnable(entity.isEnable())
                .setType(entity.getType());
    }

    @Nullable
    public static Account convert(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Account()
                .setId(entity.getId())
                .setNumber(entity.getNumber())
                .setBalance(entity.getBalance())
                .setCreatedDate(entity.getCreatedDate())
                .setEnable(entity.isEnable())
                .setOwner(convert(entity.getOwner()))
                .setPan(entity.getPan())
                .setTerminals(entity.getTerminals().stream()
                    .map(t -> t.getId())
                    .collect(Collectors.toSet()))
                .setDebitTransactions(entity.getDebitTransactions().stream()
                        .map(t -> t.getId())
                        .collect(Collectors.toSet()))
                .setCreditTransactions(entity.getCreditTransactions().stream()
                        .map(t -> t.getId())
                        .collect(Collectors.toSet()));

    }

    @Nullable
    public static AccountEntity convert(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountEntity()
                .setId(account.getId())
                .setBalance(account.getBalance())
                .setCreatedDate(account.getCreatedDate())
                .setEnable(account.isEnable())
                .setNumber(account.getNumber())
                .setOwner(convert(account.getOwner()))
                .setPan(account.getPan());
    }

    @Nullable
    public static AccountOwnerEntity convert(AccountOwner accountOwner) {
        if (accountOwner == null) {
            return null;
        }
        return new AccountOwnerEntity()
                .setId(accountOwner.getId())
                .setFullName(accountOwner.getFullName())
                .setType(accountOwner.getType())
                .setCreatedDate(accountOwner.getCreatedDate())
                .setEnable(accountOwner.isEnable());
    }
}
