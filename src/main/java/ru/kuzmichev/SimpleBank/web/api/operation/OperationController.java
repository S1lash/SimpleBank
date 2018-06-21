package ru.kuzmichev.SimpleBank.web.api.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwnerService;

@RestController
@RequestMapping(value = "operation")
public class OperationController {

    @Autowired
    private AccountOwnerService accountOwnerService;
}
