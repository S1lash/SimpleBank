package ru.kuzmichev.SimpleBank.server.service.transaction.repository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.TransactionState;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TRANSACTION")
@EqualsAndHashCode(of = {"id", "state", "type"})
@ToString
@Accessors(chain = true)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionSequence")
    @SequenceGenerator(name = "transactionSequence", sequenceName = "SEQ_TRANSACTION", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState state;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private Date createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DEBIT_ACCOUNT", nullable = false)
    private AccountEntity debitAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREDIT_ACCOUNT", nullable = false)
    private AccountEntity creditAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
}
