package com.swedbank.account.rest;

import com.swedbank.account.application.dto.AccountDto;
import com.swedbank.account.application.dto.AccountTransactionRequest;
import com.swedbank.account.application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.url.prefix}${api.version}/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/deposit")
    public ResponseEntity<AccountDto> getUserAssociationByIdsPair(
            @RequestBody AccountTransactionRequest accountTransactionRequest,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().body(
                accountService.depositMoney(accountTransactionRequest, user.getUsername()));
    }

}
