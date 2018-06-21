package ru.kuzmichev.SimpleBank.server.service.accountowner.repository;


import lombok.Getter;
import ru.kuzmichev.SimpleBank.server.service.account.repository.AccountEntity;
import ru.kuzmichev.SimpleBank.server.util.OwnerType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Entity
@Table(name = "ACCOUNT_OWNER")
public class AccountOwnerEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType type;

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private boolean enable;

    @OneToMany(mappedBy = "owner")
    private Set<AccountEntity> accounts;

    public AccountOwnerEntity() {
        this.createdDate = new Date();
        this.enable = true;
    }
}
