package com.swedbank.common.application.Dto;

import com.swedbank.common.domian.Money;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyDTO {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO-4217 code")
    private String currency;

    public static MoneyDTO toDto(Money money) {
        return new MoneyDTO(money.getAmount(), money.getCurrency().getCurrencyCode());
    }

    public static Money fromDto(MoneyDTO dto) {
        return Money.of(dto.getAmount(), Currency.getInstance(dto.getCurrency()));
    }
}
