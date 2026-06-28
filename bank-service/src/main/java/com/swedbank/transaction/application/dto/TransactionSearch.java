package com.swedbank.transaction.application.dto;

import com.swedbank.transaction.domain.model.TransactionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TransactionSearch {

    @Min(1)
    @Max(100)
    private Integer size = 50;

    private Integer page = 0;

    private Set<TransactionType> transactionTypes;

}
