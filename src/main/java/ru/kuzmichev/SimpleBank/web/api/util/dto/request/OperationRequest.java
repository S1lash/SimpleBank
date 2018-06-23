package ru.kuzmichev.SimpleBank.web.api.util.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OperationRequest {
    @NotNull
    private AccountInfo creditPart;
    @NotNull
    private AccountInfo debitPart;
    @NotNull
    @Min(0L)
    private Long amount;
}
