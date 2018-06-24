package ru.kuzmichev.SimpleBank.web.api.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kuzmichev.SimpleBank.server.service.account.AccountService;
import ru.kuzmichev.SimpleBank.server.service.accountowner.AccountOwnerService;
import ru.kuzmichev.SimpleBank.server.service.terminal.TerminalService;
import ru.kuzmichev.SimpleBank.server.service.transaction.TransactionService;
import ru.kuzmichev.SimpleBank.web.api.info.dto.Account;
import ru.kuzmichev.SimpleBank.web.api.info.dto.AccountOwner;
import ru.kuzmichev.SimpleBank.web.api.info.dto.Terminal;
import ru.kuzmichev.SimpleBank.web.api.info.dto.Transaction;
import ru.kuzmichev.SimpleBank.web.api.util.dto.request.EntityRequest;
import ru.kuzmichev.SimpleBank.web.api.util.dto.response.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "info")
public class InfoController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountOwnerService accountOwnerService;
    @Autowired
    private TerminalService terminalService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response accounts(@RequestBody(required = false) EntityRequest request) {
        log.debug("Incoming request: [{}]", request);
        List<Long> ids = request != null ? request.getIds() : Collections.EMPTY_LIST;
        return new AccountResponse()
                .setAccounts(accountService.getAllByIds(ids).stream()
                        .map(a -> new Account()
                            .setId(a.getId())
                            .setBalance(a.getBalance())
                            .setCreatedDate(a.getCreatedDate())
                            .setEnable(a.isEnable())
                            .setNumber(a.getNumber())
                            .setPan(a.getPan())
                            .setOwnerId(a.getOwner().getId()))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "accountOwners", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response accountOwners(@RequestBody(required = false) EntityRequest request) {
        log.debug("Incoming request: [{}]", request);
        List<Long> ids = request != null ? request.getIds() : Collections.EMPTY_LIST;
        return new AccountOwnerResponse()
                .setAccountOwners(accountOwnerService.getAllByIds(ids).stream()
                    .map(ao -> new AccountOwner()
                        .setId(ao.getId())
                        .setFullName(ao.getFullName())
                        .setCreatedDate(ao.getCreatedDate())
                        .setEnable(ao.isEnable())
                        .setType(ao.getType()))
                    .collect(Collectors.toList()));
    }

    @RequestMapping(value = "terminals", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response terminals(@RequestBody(required = false) EntityRequest request) {
        log.debug("Incoming request: [{}]", request);
        List<Long> ids = request != null ? request.getIds() : Collections.EMPTY_LIST;
        return new TerminalResponse()
                .setTerminals(terminalService.getAllByIds(ids).stream()
                    .map(t -> new Terminal()
                        .setId(t.getId())
                        .setAddress(t.getAddress())
                        .setCreatedDate(t.getCreatedDate())
                        .setEnable(t.isEnable())
                        .setType(t.getType())
                        .setAccountId(t.getAccount().getId()))
                    .collect(Collectors.toList()));
    }

    @RequestMapping(value = "transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response transactions(@RequestBody(required = false) EntityRequest request) {
        log.debug("Incoming request: [{}]", request);
        List<Long> ids = request != null ? request.getIds() : Collections.EMPTY_LIST;
        return new TransactionResponse()
                .setTransactions(transactionService.getAllByIds(ids).stream()
                        .map(t -> new Transaction()
                            .setId(t.getId())
                            .setAmount(t.getAmount())
                            .setCreatedDate(t.getCreatedDate())
                            .setCreditAccountId(t.getCreditAccount().getId())
                            .setDebitAccountId(t.getDebitAccount().getId())
                            .setState(t.getState())
                            .setType(t.getType()))
                        .collect(Collectors.toList()));
    }
}
