package ru.kuzmichev.SimpleBank.server.service.transaction;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.service.account.AccountService;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwner;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwnerService;
import ru.kuzmichev.SimpleBank.server.service.accountowner.SimpleAccount;
import ru.kuzmichev.SimpleBank.server.service.terminal.Terminal;
import ru.kuzmichev.SimpleBank.server.service.terminal.TerminalService;
import ru.kuzmichev.SimpleBank.server.util.AccountInfo;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;
import ru.kuzmichev.SimpleBank.server.util.exception.ClientAccountException;
import ru.kuzmichev.SimpleBank.server.util.exception.RequestValidationException;
import ru.kuzmichev.SimpleBank.server.util.request.*;
import ru.kuzmichev.SimpleBank.server.util.response.TransactionOperationResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionOperationService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TerminalService terminalService;
    @Autowired
    private TransactionOperationFacade transactionOperationFacade;
    @Autowired
    private AccountOwnerService accountOwnerService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionOperationResponse transfer(TransactionRequest request) throws RequestValidationException, ClientAccountException {
        validate(request);

        Account creditAccount = getActualAccount(getAvailableAccount(
                request.getCreditPart().getClientId(),
                request.getCreditPart().getAccountNumber()));
        if (creditAccount == null) {
            return buildErrorResponse(String.format("CreditAccount [accountInfo=] not found", request.getCreditPart()),
                    request.getTransactionType());
        }

        Account debitAccount = getActualAccount(getAvailableAccount(
                request.getDebitPart().getClientId(),
                request.getDebitPart().getAccountNumber()));
        if (debitAccount == null) {
            return buildErrorResponse(String.format("DebitAccount [accountInfo=] not found", request.getDebitPart()),
                    request.getTransactionType());
        }

        return transactionOperationFacade.doTransaction(request, creditAccount, debitAccount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionOperationResponse deposit(TransactionRequest request) throws RequestValidationException, ClientAccountException {
        validate(request);

        Account creditAccount = getAvailableTerminalAccount(request.getCreditPart().getTerminalId());
        if (creditAccount == null) {
            return buildErrorResponse(String.format("CreditAccount [accountInfo=] not found", request.getCreditPart()),
                    request.getTransactionType());
        }

        Account debitAccount = getActualAccount(getAvailableAccount(
                request.getDebitPart().getClientId(),
                request.getDebitPart().getAccountNumber()));
        if (debitAccount == null) {
            return buildErrorResponse(String.format("DebitAccount [accountInfo=] not found", request.getDebitPart()),
                    request.getTransactionType());
        }

        return transactionOperationFacade.doTransaction(request, creditAccount, debitAccount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionOperationResponse withdrawal(TransactionRequest request) throws RequestValidationException, ClientAccountException {
        validate(request);

        Account creditAccount = getActualAccount(getAvailableAccount(
                request.getCreditPart().getClientId(),
                request.getCreditPart().getAccountNumber()));
        if (creditAccount == null) {
            return buildErrorResponse(String.format("CreditAccount [accountInfo=] not found", request.getCreditPart()),
                    request.getTransactionType());
        }

        Account debitAccount = getAvailableTerminalAccount(request.getDebitPart().getTerminalId());
        if (debitAccount == null) {
            return buildErrorResponse(String.format("DebitAccount [accountInfo=] not found", request.getDebitPart()),
                    request.getTransactionType());
        }

        return transactionOperationFacade.doTransaction(request, creditAccount, debitAccount);
    }

    private Set<Account> getAvailableAccount(Long clientId, String accountNumber) throws ClientAccountException {
        AccountOwner client = null;
        if (clientId != null) {
            client = accountOwnerService.getAvailableAccountOwnerById(clientId);
        }

        if (client != null && StringUtils.isNotBlank(accountNumber)
                && !client.getAccounts().contains(accountNumber)) {
            throw new ClientAccountException(String.format(
                    "Client [id=%s] doesn't have such account [accountNumber=%s]",
                    clientId, accountNumber));
        }

        Account account = null;
        if (StringUtils.isNotBlank(accountNumber)) {
            account = accountService.getAvailableAccountByNumber(accountNumber);
        }

        if (client == null && account == null) {
            return Collections.EMPTY_SET;
        }

        if (account == null) {
            return accountService.getAvailableAccountsByNumbers(
                    client.getAccounts().stream().map(SimpleAccount::getNumber).collect(Collectors.toList()));
        }

        return Collections.singleton(account);
    }

    @Nullable
    private Account getAvailableTerminalAccount(long terminalId) {
        Terminal terminal = terminalService.getAvailableTerminalById(terminalId);
        if (terminal != null) {
            return terminal.getAccount();
        }
        return null;
    }

    @Nullable
    private Account getActualAccount(Set<Account> accounts) {
        //it's simple strategy for getting an account
        return accounts.stream()
                .filter(a -> a.isEnable())
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .findFirst()
                .orElse(null);
    }

    private void validate(TransactionRequest request) throws RequestValidationException {
        if (request.getCreditPart() == null) {
            throw new RequestValidationException("CreditPart is null");
        }
        if (request.getDebitPart() == null) {
            throw new RequestValidationException("DebitPart is null");
        }
        if (request.getAmount() < 0) {
            throw new RequestValidationException("Invalid [amount]");
        }

        switch (request.getTransactionType()) {
            case DEPOSIT:
                validateDepositOperation(request);
                break;
            case WITHDRAWAL:
                validateWithdrawalOperation(request);
                break;
            case TRANSFER:
                validateTransferOperation(request);
                break;
        }
    }

    private void validateDepositOperation(TransactionRequest request) throws RequestValidationException {
        AccountInfo debitPart = request.getDebitPart();
        AccountInfo creditPart = request.getCreditPart();

        if (StringUtils.isNotBlank(creditPart.getAccountNumber()) ||
                creditPart.getClientId() != null ||
                creditPart.getTerminalId() == null) {
            throw new RequestValidationException("CreditPart: must contain only [terminalId]");
        }
        if (debitPart.getTerminalId() != null ||
                (StringUtils.isBlank(debitPart.getAccountNumber()) && debitPart.getClientId() == null)) {
            throw new RequestValidationException("DebitPart: must contain [clientId] or [accountNumber]");
        }
    }

    private void validateWithdrawalOperation(TransactionRequest request) throws RequestValidationException {
        AccountInfo debitPart = request.getDebitPart();
        AccountInfo creditPart = request.getCreditPart();

        if (creditPart.getTerminalId() != null ||
                (StringUtils.isBlank(creditPart.getAccountNumber()) && creditPart.getClientId() == null)) {
            throw new RequestValidationException("CreditPart: must contain [clientId] or [accountNumber]");
        }
        if (StringUtils.isNotBlank(debitPart.getAccountNumber()) ||
                debitPart.getClientId() != null ||
                debitPart.getTerminalId() == null) {
            throw new RequestValidationException("DebitPart: must contain only [terminalId]");
        }
    }

    private void validateTransferOperation(TransactionRequest request) throws RequestValidationException {
        AccountInfo debitPart = request.getDebitPart();
        AccountInfo creditPart = request.getCreditPart();

        if (creditPart.getTerminalId() != null ||
                (StringUtils.isBlank(creditPart.getAccountNumber()) && creditPart.getClientId() == null)) {
            throw new RequestValidationException("CreditPart: must contain [clientId] or [accountNumber]");
        }
        if (debitPart.getTerminalId() != null ||
                (StringUtils.isBlank(debitPart.getAccountNumber()) && debitPart.getClientId() == null)) {
            throw new RequestValidationException("DebitPart: must contain [clientId] or [accountNumber]");
        }
    }

    private TransactionOperationResponse buildErrorResponse(String message, TransactionType transactionType) {
        return new TransactionOperationResponse()
                .setTransactionType(transactionType)
                .setError(true)
                .setErrorMessage(message);
    }
}
