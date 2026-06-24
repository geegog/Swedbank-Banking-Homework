package com.swedbank.account.application.service;

import com.swedbank.account.application.dto.AccountDto;
import com.swedbank.account.application.dto.AccountTransaction;
import com.swedbank.account.domian.model.Account;
import com.swedbank.account.domian.repository.AccountRepository;
import com.swedbank.common.domian.Money;
import com.swedbank.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.Set;

@Setter
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private Account getAccountByNumberAndUser(String accountNumber, String userEmail) {
        return accountRepository.finByAccountNumberAndUser_Email(accountNumber, userEmail)
                .orElseThrow(() -> new RuntimeException("Account not found for user"));
    }

    private Set<Account> getAccountsByUser(String userEmail) {
        return accountRepository.findByUser_Email(userEmail);
    }

    public void depositMoney(AccountTransaction accountTransaction, String email) {

        var user = userService.getUserByEmail(email);

        var account = getAccountByNumberAndUser(accountTransaction.getAccountNumber(), user.getEmail());

        var newBalance = account.getBalance().getAmount() != null ? account.getBalance().getAmount().add(accountTransaction.getValue().getAmount()) : null;
        account.setBalance(Money.of(newBalance, account.getBalance().getCurrency()));

        accountRepository.save(account);

    }

    public void withdrawMoney(AccountTransaction accountTransaction, String email) {

        var user = userService.getUserByEmail(email);

        var account = getAccountByNumberAndUser(accountTransaction.getAccountNumber(), user.getEmail());

        if (account.getBalance().getAmount() == null || account.getBalance().getAmount().compareTo(accountTransaction.getValue().getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        var newBalance = account.getBalance().getAmount().subtract(accountTransaction.getValue().getAmount());
        account.setBalance(Money.of(newBalance, account.getBalance().getCurrency()));

        accountRepository.save(account);

    }

    public AccountDto getAccountBalance(AccountTransaction accountTransaction, String email) {

        var user = userService.getUserByEmail(email);

        var account = getAccountByNumberAndUser(accountTransaction.getAccountNumber(), user.getEmail());

        return modelMapper.map(account, AccountDto.class);

    }

    public Set<AccountDto> getAccountsByUserEmail(String email) {
        var user = userService.getUserByEmail(email);

        var accounts = getAccountsByUser(user.getEmail());

        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(java.util.stream.Collectors.toSet());
    }

}
