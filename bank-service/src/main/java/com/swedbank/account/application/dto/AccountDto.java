package com.swedbank.account.application.dto;

import com.swedbank.common.application.dto.BaseDto;
import com.swedbank.common.application.dto.MoneyDto;
import com.swedbank.user.application.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto extends BaseDto {

    private String accountNumber;

    private String accountName;

    private MoneyDto balance;

    private UserDto user;

}
