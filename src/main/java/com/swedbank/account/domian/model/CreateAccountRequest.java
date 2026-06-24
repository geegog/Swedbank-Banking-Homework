package com.swedbank.account.domian.model;

import com.swedbank.account.application.annotation.AllowedCurrency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
public class CreateAccountRequest {

    @NotNull(message = "Account number is required")
    private String accountName;

    @NotNull(message = "Currency is required")
    @AllowedCurrency
    private Currency currency;

}
