package ru.kuzmichev.SimpleBank.server.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kuzmichev.SimpleBank.server.service.account.Account;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.request.TransactionRequest;
import ru.kuzmichev.SimpleBank.server.util.response.TransactionOperationResponse;

@Service
public class TransactionOperationFacade {

    @Autowired
    private TransactionService transactionService;

    @Transactional(propagation = Propagation.MANDATORY)
    public TransactionOperationResponse doTransaction(TransactionRequest request, Account creditAccount, Account debitAccount) {
        Transaction transaction = new Transaction()
                .setAmount(request.getAmount())
                .setType(request.getTransactionType())
                .setDebitAccount(debitAccount)
                .setCreditAccount(creditAccount);
        // save trx (state = created)
        transactionService.saveTransaction(transaction);

        boolean result = transactionService.calculateOperation(transaction);
        TransactionOperationResponse response = buildResponse(transaction, result);

        // update trx
        transactionService.saveTransaction(transaction);

        return response;
    }

    private TransactionOperationResponse buildResponse(Transaction transaction, boolean result) {
        TransactionOperationResponse response = new TransactionOperationResponse();
        if (!result) {
            transaction.setState(TransactionState.DECLINE);
            response.setErrorMessage("Not enough money");
        } else {
            transaction.setState(TransactionState.APPROVED);
        }
        return response
                .setError(!result)
                .setTransactionId(transaction.getId())
                .setTransactionState(transaction.getState())
                .setTransactionType(transaction.getType());
    }
}
