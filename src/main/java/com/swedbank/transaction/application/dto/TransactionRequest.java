package com.swedbank.transaction.application.dto;

import com.swedbank.common.application.Dto.MoneyDto;
import com.swedbank.transaction.domian.model.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TransactionRequest {

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @Valid
    private MoneyDto value;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    private String reference;

}
