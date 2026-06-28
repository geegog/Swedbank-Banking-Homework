package com.swedbank.transaction.rest;

import com.swedbank.transaction.application.dto.PagedResult;
import com.swedbank.transaction.application.dto.TransactionDto;
import com.swedbank.transaction.application.dto.TransactionSearch;
import com.swedbank.transaction.application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.url.prefix}${api.version}/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/account/{accountNumber}/history")
    public ResponseEntity<PagedResult<TransactionDto>> moveMoneyBetweenAccounts(
            @RequestBody @Valid TransactionSearch transactionSearch,
            @AuthenticationPrincipal User user,
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactions(transactionSearch, accountNumber, user.getUsername()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionDto> getTransactions(@PathVariable UUID id,
                                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getTransaction(id, user.getUsername()));
    }

}
