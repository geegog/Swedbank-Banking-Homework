package com.swedbank.transaction.application.service;

import com.swedbank.common.domian.Money;
import com.swedbank.transaction.application.dto.TransactionDto;
import com.swedbank.transaction.application.dto.TransactionRequest;
import com.swedbank.transaction.domian.model.Transaction;
import com.swedbank.transaction.domian.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final ModelMapper modelMapper;

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void recordTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setUserId(transactionRequest.getUserId());
        transaction.setAccountNumber(transactionRequest.getAccountNumber());
        if (transactionRequest.getReference() != null) {
            transaction.setReference(transactionRequest.getReference());
        }
        transaction.setValue(Money.of(transactionRequest.getValue().getAmount(), transactionRequest.getValue().getCurrency()));
    }

    public List<TransactionDto> getTransactions(String accountNumber, UUID userId) {

        var transactions = transactionRepository.findByAccountNumberAndUserId(accountNumber, userId);

        return modelMapper.map(transactions, new TypeToken<List<TransactionDto>>() {}.getType());
    }

}
