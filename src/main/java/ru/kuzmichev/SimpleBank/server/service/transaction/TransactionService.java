package ru.kuzmichev.SimpleBank.server.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.kuzmichev.SimpleBank.server.service.transaction.repository.TransactionRepository;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAccount(Transaction transaction) {
        Assert.notNull(transaction, "transaction is null");
        Assert.notNull(transaction.getDebitAccount(), "transaction.debitAccount is null");
        Assert.notNull(transaction.getCreditAccount(), "transaction.creditAccount is null");

        transactionRepository.save(convert(transaction));
    }
}
