package com.ashish.accounttransactionservice.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        try {

            return transactionRepository.save(transaction);

        } catch (Exception e) {
            throw new TransactionServiceException("Exception occurred while saving player transaction", e);
        }
    }


    public List<Transaction> getTransactions(long playerId) {
        try {

            return transactionRepository.findAll().stream()
                    .filter(transaction -> playerId == transaction.getPlayerId()).collect(Collectors.toList());

        } catch (Exception e) {
            throw new TransactionServiceException("Exception occurred while fetching player transactions", e);
        }
    }

    public boolean isTransactionExists(long transactionId) {
        try {
            return transactionRepository.existsById(transactionId);
        } catch (Exception e) {
            throw new TransactionServiceException("Exception occurred while checking player transactions", e);
        }
    }

}
