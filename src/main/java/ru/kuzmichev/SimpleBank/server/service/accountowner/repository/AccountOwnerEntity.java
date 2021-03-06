package ru.kuzmichev.SimpleBank.server.service.accountowner.repository;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.OwnerType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "ACCOUNT_OWNER")
@EqualsAndHashCode(of = {"id", "fullName"})
@ToString(exclude = "accounts")
@Accessors(chain = true)
public class AccountOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountOwnerSequence")
    @SequenceGenerator(name = "accountOwnerSequence", sequenceName = "SEQ_ACCOUNT_OWNER", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType type;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private boolean enable;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<AccountEntity> accounts;
}
