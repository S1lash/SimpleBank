package ru.kuzmichev.SimpleBank.server.service.account.repository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.accountowner.repository.AccountOwnerEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;
import ru.kuzmichev.SimpleBank.server.service.transaction.repository.TransactionEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "ACCOUNT")
@EqualsAndHashCode(of = {"id", "number"})
@ToString(exclude = {"debitTransactions", "creditTransactions", "terminals"})
@Accessors(chain = true)
public class AccountEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private long balance;

    @Column(nullable = true)
    private String pan;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private boolean enable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private AccountOwnerEntity owner;

    @OneToMany(mappedBy = "debitAccount")
    private Set<TransactionEntity> debitTransactions;

    @OneToMany(mappedBy = "creditAccount")
    private Set<TransactionEntity> creditTransactions;

    @OneToMany(mappedBy = "account")
    private Set<TerminalEntity> terminals;

    public AccountEntity() {
        this.createdDate = new Date();
        this.enable = true;
        this.balance = 0L;
    }
}
