package ru.kuzmichev.SimpleBank.server.service.terminal.repository;

import lombok.Getter;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.TerminalType;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Table(name = "TERMINAL")
public class TerminalEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = true)
    private String adress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TerminalType type;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private boolean enable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private AccountEntity account;

    public TerminalEntity() {
        this.createdDate = new Date();
        this.enable = true;
    }
}
