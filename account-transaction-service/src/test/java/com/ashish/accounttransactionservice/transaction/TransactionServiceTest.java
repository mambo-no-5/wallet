package com.ashish.accounttransactionservice.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void saveTransactionTest() {

        Transaction expectedTransaction = new Transaction(9999000L, 999L, 1.0,
                TransactionType.DEBIT, Instant.now().toString());

        when(transactionRepository.save(expectedTransaction)).thenReturn(expectedTransaction);

        Transaction actualTransactionResponse = transactionService.saveTransaction(expectedTransaction);

        assertEquals(expectedTransaction, actualTransactionResponse);
        verify(transactionRepository, Mockito.times(1)).save(expectedTransaction);
    }

    @Test
    void getTransactionsTest() {

        Transaction expectedTransaction1 = new Transaction(9999000L, 999L, 1.5,
                TransactionType.DEBIT, Instant.now().toString());

        Transaction expectedTransaction2 = new Transaction(9458L, 111L, 2.5,
                TransactionType.CREDIT, Instant.now().toString());


        when(transactionRepository.findAll()).thenReturn(List.of(expectedTransaction1, expectedTransaction2));

        List<Transaction> actualTransactionResponse = transactionService.getTransactions(999L);

        assertTrue(actualTransactionResponse.contains(expectedTransaction1));
        verify(transactionRepository, Mockito.times(1)).findAll();


    }

    @Test
    void transactionExistsTest() {

        when(transactionRepository.existsById(9999000L)).thenReturn(true);

        assertTrue(transactionService.isTransactionExists(9999000L));
        verify(transactionRepository, Mockito.times(1)).existsById(9999000L);

    }

    @Test
    void saveTransactionTestTransactionServiceException() {

        Transaction transaction = new Transaction(9999000L, 999L, 1.5,
                TransactionType.DEBIT, Instant.now().toString());

        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.saveTransaction(transaction));

        verify(transactionRepository, Mockito.times(1)).save(transaction);
    }

    @Test
    void getTransactionsTestTransactionServiceException() {

        when(transactionRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.getTransactions(999L));

        verify(transactionRepository, Mockito.times(1)).findAll();

    }

    @Test
    void transactionExistsTestTransactionServiceException() {

        when(transactionRepository.existsById(9999000L)).thenThrow(new RuntimeException());

        assertThrows(TransactionServiceException.class, () -> transactionService.isTransactionExists(9999000L));

        verify(transactionRepository, Mockito.times(1)).existsById(9999000L);

    }
}