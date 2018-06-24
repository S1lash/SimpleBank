package ru.kuzmichev.SimpleBank.server.service.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.transaction.repository.TransactionRepository;
import ru.kuzmichev.SimpleBank.server.util.CalculationResult;
import ru.kuzmichev.SimpleBank.server.util.exception.BalanceOperationException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveTransaction(Transaction transaction) {
        log.debug("Save transaction [{}]", transaction);
        Assert.notNull(transaction, "transaction is null");
        Assert.notNull(transaction.getDebitAccount(), "transaction.debitAccount is null");
        Assert.notNull(transaction.getCreditAccount(), "transaction.creditAccount is null");

        long transactionId = transactionRepository.save(convert(transaction)).getId();
        transaction.setId(transactionId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public CalculationResult calculateOperation(Transaction transaction) {
        log.debug("Start calculate balnce operation for transaction [{}]", transaction);
        long creditStartBalance = transaction.getCreditAccount().getBalance();
        long debitStartBalance = transaction.getDebitAccount().getBalance();
        try {
            validateCalculateOperation(creditStartBalance, debitStartBalance, transaction.getAmount());
            transaction.getDebitAccount().increaseBalance(transaction.getAmount());
            transaction.getCreditAccount().decreaseBalance(transaction.getAmount());
        } catch (Exception e) {
            transaction.getCreditAccount().setBalance(creditStartBalance);
            transaction.getDebitAccount().setBalance(debitStartBalance);
            return new CalculationResult().setError(true).setErrorMessage(e.getMessage());
        }
        return new CalculationResult().setError(false);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllByIds(List<Long> ids) {
        log.debug("Get all transactions with ids [{}]", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return transactionRepository.findAll().stream()
                    .map(t -> convert(t))
                    .collect(Collectors.toList());
        }
        return transactionRepository.findAllById(ids).stream()
                .map(t -> convert(t))
                .collect(Collectors.toList());
    }

    private void validateCalculateOperation(long creditBalance, long debitBalance, long amount) throws BalanceOperationException {
        if (creditBalance < amount) {
            throw new BalanceOperationException("Not enough money");
        }
        if ((Long.MAX_VALUE - debitBalance) < amount) {
            throw new BalanceOperationException("Amount is too large");
        }
    }
}
