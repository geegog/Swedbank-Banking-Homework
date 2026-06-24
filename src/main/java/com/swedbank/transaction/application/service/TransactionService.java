package com.swedbank.transaction.application.service;

import com.swedbank.common.domian.Money;
import com.swedbank.transaction.application.dto.TransactionRequest;
import com.swedbank.transaction.domian.model.Transaction;
import com.swedbank.transaction.domian.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

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

}
