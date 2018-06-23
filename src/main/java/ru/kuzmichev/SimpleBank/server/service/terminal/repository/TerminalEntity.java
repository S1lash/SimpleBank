package ru.kuzmichev.SimpleBank.server.service.terminal.repository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.TerminalType;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TERMINAL")
@EqualsAndHashCode(of = {"id", "address"})
@ToString
@Accessors(chain = true)
public class TerminalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terminalSequence")
    @SequenceGenerator(name = "terminalSequence", sequenceName = "SEQ_TERMINAL", allocationSize = 1)
    private Long id;

    @Column
    private String address;

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
}
