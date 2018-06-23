package ru.kuzmichev.SimpleBank.server.util;

import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwner;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.Terminal;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;
import ru.kuzmichev.SimpleBank.server.service.transaction.Transaction;
import ru.kuzmichev.SimpleBank.server.service.transaction.repository.TransactionEntity;

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
                    .map(a -> convert(a))
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
                .setPan(entity.getPan());

    }

    @Nullable
    public static Transaction convert(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Transaction()
                .setId(entity.getId())
                .setAmount(entity.getAmount())
                .setCreatedDate(entity.getCreatedDate())
                .setDebitAccount(convert(entity.getDebitAccount()))
                .setCreditAccount(convert(entity.getCreditAccount()))
                .setState(entity.getState())
                .setType(entity.getType());
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

    @Nullable
    public static TerminalEntity convert(Terminal terminal) {
        if (terminal == null) {
            return null;
        }
        return new TerminalEntity()
                .setId(terminal.getId())
                .setAccount(convert(terminal.getAccount()))
                .setAddress(terminal.getAddress())
                .setCreatedDate(terminal.getCreatedDate())
                .setEnable(terminal.isEnable())
                .setType(terminal.getType());
    }

    @Nullable
    public static TransactionEntity convert(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionEntity()
                .setId(transaction.getId())
                .setAmount(transaction.getAmount())
                .setCreatedDate(transaction.getCreatedDate())
                .setDebitAccount(convert(transaction.getDebitAccount()))
                .setCreditAccount(convert(transaction.getCreditAccount()))
                .setState(transaction.getState())
                .setType(transaction.getType());
    }
}
