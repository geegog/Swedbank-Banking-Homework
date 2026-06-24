package com.swedbank.account.application.service;

import com.swedbank.account.domian.repository.AccountRepository;
import com.swedbank.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Setter
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

}
