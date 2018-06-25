package ru.kuzmichev.SimpleBank.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import ru.kuzmichev.SimpleBank.ApiTest;
import ru.kuzmichev.SimpleBank.ContentBuilder;
import ru.kuzmichev.SimpleBank.PerformBuilder;
import ru.kuzmichev.SimpleBank.RestMethods;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.service.account.AccountService;
import ru.kuzmichev.SimpleBank.server.service.transaction.Transaction;
import ru.kuzmichev.SimpleBank.server.service.transaction.TransactionService;
import ru.kuzmichev.SimpleBank.server.util.AccountInfo;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;
import ru.kuzmichev.SimpleBank.server.util.request.TransactionRequest;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class OperationControllerTest extends ApiTest {
    //todo: add mockito objects, add more tests
    private static final long DEBIT_ACCOUNT_ID = 4L;
    private static final String DEBIT_ACCOUNT_NUMBER = "0000000000000000004";
    private static final long CREDIT_ACCOUNT_ID = 6L;
    private static final String CREDIT_ACCOUNT_NUMBER = "0000000000000000006";
    private static final long TERMINAL_ID = 1L;
    private static final String TERMINAL_ACCOUNT_NUMBER = "0000000000000000000";
    private static final long AMOUNT = 70000L;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Test
    public void transferSuccessTest() throws Exception {
        Account startCreditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(startCreditAccount);
        startCreditAccount.decreaseBalance(AMOUNT);
        Account startDebitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(startDebitAccount);
        startDebitAccount.increaseBalance(AMOUNT);

        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        String response = transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.transactionType").value("TRANSFER"))
                .andReturn().getResponse().getContentAsString();
        Integer transactionId = JsonPath.read(response, "$.transactionId");

        Transaction transaction = transactionService.getAllByIds(Collections.singletonList(transactionId.longValue())).get(0);
        assertTrue(transaction.getState().equals(TransactionState.APPROVED));
        assertTrue(CREDIT_ACCOUNT_NUMBER.equals(transaction.getCreditAccount().getNumber()));
        assertTrue(DEBIT_ACCOUNT_NUMBER.equals(transaction.getDebitAccount().getNumber()));

        Account creditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(creditAccount);
        Account debitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(debitAccount);

        assertTrue(creditAccount.getBalance() == startCreditAccount.getBalance());
        assertTrue(debitAccount.getBalance() == startDebitAccount.getBalance());
    }

    @Test
    public void transferWithEmptyCreditPartFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(null)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("creditPart: must not be null"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithEmptyDebitPartFailedTest() throws Exception {
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(null)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("debitPart: must not be null"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithIncorrectAccountForClientFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber("123");
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("Client [id=6] doesn't have such account [accountNumber=123]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithIncorrectExistingAccountForClientFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber("0000000000000000001");
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("Client [id=6] doesn't have such account [accountNumber=0000000000000000001]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithTerminalIdInCreditPartFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[CreditPart] must contain [clientId] or [accountNumber]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithTerminalIdInDebitPartFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[DebitPart] must contain [clientId] or [accountNumber]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void transferWithNotExistClientFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(123L);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.transactionId").doesNotExist())
                .andExpect(jsonPath("$.description").value("DebitAccount [accountInfo=AccountInfo(clientId=123, accountNumber=null, terminalId=null)] not found"));
    }

    @Test
    public void transferWithNotExistAccountFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setAccountNumber("123");
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.transactionId").doesNotExist())
                .andExpect(jsonPath("$.description").value("DebitAccount [accountInfo=AccountInfo(clientId=null, accountNumber=123, terminalId=null)] not found"));
    }

    @Test
    public void transferNotEnoughMoneyFailedTest() throws Exception {
        Account startCreditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(startCreditAccount);
        Account startDebitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(startDebitAccount);

        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(99999999L);

        String response = transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.description").value("Not enough money"))
                .andReturn().getResponse().getContentAsString();

        Integer transactionId = JsonPath.read(response, "$.transactionId");

        Transaction transaction = transactionService.getAllByIds(Collections.singletonList(transactionId.longValue())).get(0);
        assertTrue(transaction.getState().equals(TransactionState.DECLINE));

        Account creditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(creditAccount);
        Account debitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(debitAccount);

        assertTrue(creditAccount.getBalance() == startCreditAccount.getBalance());
        assertTrue(debitAccount.getBalance() == startDebitAccount.getBalance());
    }

    @Test
    public void transferAmountIsTooLargeFailedTest() throws Exception {
        Account startCreditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(startCreditAccount);
        Account startDebitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(startDebitAccount);

        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber("0000000000000000009");
        TransactionRequest request = new TransactionRequest(TransactionType.TRANSFER)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(Long.MAX_VALUE - 1);

        String response = transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.description").value("Amount is too large"))
                .andReturn().getResponse().getContentAsString();

        Integer transactionId = JsonPath.read(response, "$.transactionId");

        Transaction transaction = transactionService.getAllByIds(Collections.singletonList(transactionId.longValue())).get(0);
        assertTrue(transaction.getState().equals(TransactionState.DECLINE));

        Account creditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(creditAccount);
        Account debitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(debitAccount);

        assertTrue(creditAccount.getBalance() == startCreditAccount.getBalance());
        assertTrue(debitAccount.getBalance() == startDebitAccount.getBalance());
    }

    @Test
    public void depositSuccessTest() throws Exception {
        Account startCreditAccount = accountService.getAllByIds(Collections.singletonList(TERMINAL_ID)).get(0);
        assertNotNull(startCreditAccount);
        startCreditAccount.decreaseBalance(AMOUNT);
        Account startDebitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(startDebitAccount);
        startDebitAccount.increaseBalance(AMOUNT);

        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER);
        AccountInfo creditPart = new AccountInfo()
                .setTerminalId(TERMINAL_ID);
        TransactionRequest request = new TransactionRequest(TransactionType.DEPOSIT)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        String response = transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"))
                .andReturn().getResponse().getContentAsString();
        Integer transactionId = JsonPath.read(response, "$.transactionId");

        Transaction transaction = transactionService.getAllByIds(Collections.singletonList(transactionId.longValue())).get(0);
        assertTrue(transaction.getState().equals(TransactionState.APPROVED));
        assertTrue(TERMINAL_ACCOUNT_NUMBER.equals(transaction.getCreditAccount().getNumber()));
        assertTrue(DEBIT_ACCOUNT_NUMBER.equals(transaction.getDebitAccount().getNumber()));

        Account creditAccount = accountService.getAllByIds(Collections.singletonList(TERMINAL_ID)).get(0);
        assertNotNull(creditAccount);
        Account debitAccount = accountService.getAllByIds(Collections.singletonList(DEBIT_ACCOUNT_ID)).get(0);
        assertNotNull(debitAccount);

        assertTrue(creditAccount.getBalance() == startCreditAccount.getBalance());
        assertTrue(debitAccount.getBalance() == startDebitAccount.getBalance());
    }

    @Test
    public void depositWithoutTerminalIdInCreditPartFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setTerminalId(TERMINAL_ID);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        TransactionRequest request = new TransactionRequest(TransactionType.DEPOSIT)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[CreditPart] must contain only [terminalId]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void depositWithTerminalIdInDebitPartFailedTest() throws Exception {
        AccountInfo debitPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        AccountInfo creditPart = new AccountInfo()
                .setTerminalId(0L);
        TransactionRequest request = new TransactionRequest(TransactionType.DEPOSIT)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[DebitPart] must contain [clientId] or [accountNumber]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void withdrawalSuccessTest() throws Exception {
        Account startCreditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(startCreditAccount);
        startCreditAccount.decreaseBalance(AMOUNT);
        Account startDebitAccount = accountService.getAllByIds(Collections.singletonList(TERMINAL_ID)).get(0);
        assertNotNull(startDebitAccount);
        startDebitAccount.increaseBalance(AMOUNT);

        AccountInfo debitPart = new AccountInfo()
                .setTerminalId(TERMINAL_ID);
        AccountInfo creditPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER);
        TransactionRequest request = new TransactionRequest(TransactionType.WITHDRAWAL)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        String response = transaction(request)
                .andExpect(successfulResponse())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.transactionType").value("WITHDRAWAL"))
                .andReturn().getResponse().getContentAsString();
        Integer transactionId = JsonPath.read(response, "$.transactionId");

        Transaction transaction = transactionService.getAllByIds(Collections.singletonList(transactionId.longValue())).get(0);
        assertTrue(transaction.getState().equals(TransactionState.APPROVED));
        assertTrue(CREDIT_ACCOUNT_NUMBER.equals(transaction.getCreditAccount().getNumber()));
        assertTrue(TERMINAL_ACCOUNT_NUMBER.equals(transaction.getDebitAccount().getNumber()));

        Account creditAccount = accountService.getAllByIds(Collections.singletonList(CREDIT_ACCOUNT_ID)).get(0);
        assertNotNull(creditAccount);
        Account debitAccount = accountService.getAllByIds(Collections.singletonList(TERMINAL_ID)).get(0);
        assertNotNull(debitAccount);

        assertTrue(creditAccount.getBalance() == startCreditAccount.getBalance());
        assertTrue(debitAccount.getBalance() == startDebitAccount.getBalance());
    }

    @Test
    public void withdrawalWithoutTerminalIdInDebitPartFailedTest() throws Exception {
        AccountInfo creditPart = new AccountInfo()
                .setTerminalId(TERMINAL_ID);
        AccountInfo debitPart = new AccountInfo()
                .setClientId(CREDIT_ACCOUNT_ID)
                .setAccountNumber(CREDIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        TransactionRequest request = new TransactionRequest(TransactionType.DEPOSIT)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[DebitPart] must contain [clientId] or [accountNumber]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    @Test
    public void withdrawalWithTerminalIdInCreditPartFailedTest() throws Exception {
        AccountInfo creditPart = new AccountInfo()
                .setClientId(DEBIT_ACCOUNT_ID)
                .setAccountNumber(DEBIT_ACCOUNT_NUMBER)
                .setTerminalId(0L);
        AccountInfo debitPart = new AccountInfo()
                .setTerminalId(0L);
        TransactionRequest request = new TransactionRequest(TransactionType.DEPOSIT)
                .setCreditPart(creditPart)
                .setDebitPart(debitPart)
                .setAmount(AMOUNT);

        transaction(request)
                .andExpect(failedResponse())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorMessage").value("[CreditPart] must contain only [terminalId]"))
                .andExpect(jsonPath("$.cause").value("validation.error"));
    }

    private ResultActions transaction(TransactionRequest request) throws Exception {
        String url = null;
        switch (request.getTransactionType()) {
            case TRANSFER:
                url = RestMethods.TRANSFER;
                break;
            case WITHDRAWAL:
                url = RestMethods.WITHDRAWAL;
                break;
            case DEPOSIT:
                url = RestMethods.DEPOSIT;
                break;
        }
        PerformBuilder builder = api(url, true);
        if (request == null) {
            return builder.call();
        }
        ContentBuilder contentBuilder = new ContentBuilder(builder);
        contentBuilder
            .add("amount", request.getAmount());
        if (request.getDebitPart() != null) {
            contentBuilder
                    .addObject("debitPart")
                        .add("terminalId", request.getDebitPart().getTerminalId(),
                                "clientId", request.getDebitPart().getClientId(),
                                "accountNumber", request.getDebitPart().getAccountNumber());
        }
        if (request.getCreditPart() != null) {
            contentBuilder
                    .addObject("creditPart")
                    .add("terminalId", request.getCreditPart().getTerminalId(),
                            "clientId", request.getCreditPart().getClientId(),
                            "accountNumber", request.getCreditPart().getAccountNumber());
        }

        return contentBuilder.call();
    }
}
