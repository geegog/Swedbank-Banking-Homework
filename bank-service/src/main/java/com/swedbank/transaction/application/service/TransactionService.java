package com.swedbank.transaction.application.service;

import com.swedbank.common.application.exception.NotFoundException;
import com.swedbank.common.domian.Money;
import com.swedbank.transaction.application.dto.PagedResult;
import com.swedbank.transaction.application.dto.TransactionDto;
import com.swedbank.transaction.application.dto.TransactionRequest;
import com.swedbank.transaction.application.dto.TransactionSearch;
import com.swedbank.transaction.domain.model.Transaction;
import com.swedbank.transaction.domain.model.TransactionSpecifications;
import com.swedbank.transaction.domain.repository.TransactionRepository;
import com.swedbank.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final ModelMapper modelMapper;
    private final UserService userService;

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void recordTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setUserId(transactionRequest.getUserId());
        transaction.setAccountNumber(transactionRequest.getAccountNumber());
        transaction.setBalance(Money.of(transactionRequest.getBalance().getAmount(),
                transactionRequest.getBalance().getCurrency()));
        if (transactionRequest.getReference() != null) {
            transaction.setReference(transactionRequest.getReference());
        }
        if (transactionRequest.getTargetValue() != null) {
            transaction.setTargetValue(Money.of(transactionRequest.getTargetValue().getAmount(),
                    transactionRequest.getTargetValue().getCurrency()));
        }
        if (transactionRequest.getTargetAccountNumber() != null) {
            transaction.setTargetAccountNumber(transactionRequest.getTargetAccountNumber());
        }
        if (transactionRequest.getExchangeRate() != null) {
            transaction.setExchangeRate(transactionRequest.getExchangeRate());
        }
        if (transactionRequest.getTargetBalance()  != null) {
            transaction.setTargetBalance(Money.of(transactionRequest.getTargetBalance().getAmount(),
                    transactionRequest.getTargetBalance().getCurrency()));
        }
        transaction.setValue(Money.of(transactionRequest.getValue().getAmount(),
                transactionRequest.getValue().getCurrency()));
        saveTransaction(transaction);
    }

    public PagedResult<TransactionDto> getTransactions(TransactionSearch transactionSearch, String accountNumber, String email) {

        var user = userService.getUserByEmail(email);

        Pageable pageable = PageRequest.of(transactionSearch.getPage(),
                transactionSearch.getSize(),
                Sort.by(Sort.Direction.DESC, "created"));

        Specification<Transaction> spec = TransactionSpecifications.buildSearchQuery(transactionSearch, user.getId(), accountNumber);

        var transactionPage = transactionRepository.findAll(spec, pageable);

        return modelMapper.map(
                transactionPage,
                new TypeToken<PagedResult<TransactionDto>>() {}.getType()
        );

    }

    public TransactionDto getTransaction(UUID transactionId, String email) {

        var user = userService.getUserByEmail(email);

        var transaction = transactionRepository.findByIdAndUserId(transactionId, user.getId()).orElseThrow(
                () -> new NotFoundException("Transaction not found")
        );
        return modelMapper.map(transaction, TransactionDto.class);
    }

}
