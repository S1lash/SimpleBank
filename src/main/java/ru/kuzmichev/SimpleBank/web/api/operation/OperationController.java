package ru.kuzmichev.SimpleBank.web.api.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kuzmichev.SimpleBank.server.service.transaction.TransactionOperationService;
import ru.kuzmichev.SimpleBank.server.util.AccountInfo;
import ru.kuzmichev.SimpleBank.server.util.TransactionType;
import ru.kuzmichev.SimpleBank.server.util.exception.ClientAccountException;
import ru.kuzmichev.SimpleBank.server.util.exception.RequestValidationException;
import ru.kuzmichev.SimpleBank.server.util.request.TransactionRequest;
import ru.kuzmichev.SimpleBank.server.util.response.TransactionOperationResponse;
import ru.kuzmichev.SimpleBank.web.api.util.dto.request.OperationRequest;
import ru.kuzmichev.SimpleBank.web.api.util.dto.response.OperationResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "operation")
public class OperationController {

    @Autowired
    private TransactionOperationService transactionOperationService;

    @RequestMapping(value = "transfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationResponse transfer(@Valid @RequestBody OperationRequest request) throws RequestValidationException, ClientAccountException {
        log.info("Incoming request: [{}]", request);
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.TRANSFER)
                .setAmount(request.getAmount())
                .setDebitPart(convert(request.getDebitPart()))
                .setCreditPart(convert(request.getCreditPart()));

        TransactionOperationResponse response = transactionOperationService.transfer(transactionRequest);

        return buildResponse(response);
    }

    @RequestMapping(value = "deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationResponse deposit(@Valid @RequestBody OperationRequest request) throws RequestValidationException, ClientAccountException {
        log.debug("Incoming request: [{}]", request);
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.DEPOSIT)
                .setAmount(request.getAmount())
                .setDebitPart(convert(request.getDebitPart()))
                .setCreditPart(convert(request.getCreditPart()));

        TransactionOperationResponse response = transactionOperationService.deposit(transactionRequest);

        return buildResponse(response);
    }

    @RequestMapping(value = "withdrawal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationResponse withdrawal(@Valid @RequestBody OperationRequest request) throws RequestValidationException, ClientAccountException {
        log.debug("Incoming request: [{}]", request);
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.WITHDRAWAL)
                .setAmount(request.getAmount())
                .setDebitPart(convert(request.getDebitPart()))
                .setCreditPart(convert(request.getCreditPart()));

        TransactionOperationResponse response = transactionOperationService.withdrawal(transactionRequest);

        return buildResponse(response);
    }

    private AccountInfo convert(ru.kuzmichev.SimpleBank.web.api.util.dto.request.AccountInfo requestAccountInfo) {
        return new AccountInfo()
                .setAccountNumber(requestAccountInfo.getAccountNumber())
                .setClientId(requestAccountInfo.getClientId())
                .setTerminalId(requestAccountInfo.getTerminalId());
    }

    private OperationResponse buildResponse(TransactionOperationResponse response) {
        OperationResponse operationResponse = new OperationResponse()
                .setError(response.isError())
                .setDescription(response.getErrorMessage())
                .setTransactionId(response.getTransactionId())
                .setTransactionType(response.getTransactionType())
                .setState(response.getTransactionState());
        log.debug("Response: [{}]", operationResponse);
        return operationResponse;
    }
}
