package ru.kuzmichev.SimpleBank.server.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.transaction.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveTransaction(Transaction transaction) {
        Assert.notNull(transaction, "transaction is null");
        Assert.notNull(transaction.getDebitAccount(), "transaction.debitAccount is null");
        Assert.notNull(transaction.getCreditAccount(), "transaction.creditAccount is null");

        long transactionId = transactionRepository.save(convert(transaction)).getId();
        transaction.setId(transactionId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean calculateOperation(Transaction transaction) {
        if (transaction.getCreditAccount().getBalance() < transaction.getAmount()) {
            return false;
        }
        long creditStartBalance = transaction.getCreditAccount().getBalance();
        long debitStartBalance = transaction.getDebitAccount().getBalance();
        try {
            transaction.getDebitAccount().increaseBalance(transaction.getAmount());
            transaction.getCreditAccount().decreaseBalance(transaction.getAmount());
        } catch (Exception e) {
            transaction.getCreditAccount().setBalance(creditStartBalance);
            transaction.getDebitAccount().setBalance(debitStartBalance);
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return transactionRepository.findAll().stream()
                    .map(t -> convert(t))
                    .collect(Collectors.toList());
        }
        return transactionRepository.findAllById(ids).stream()
                .map(t -> convert(t))
                .collect(Collectors.toList());
    }
}
