package com.ashish.wallet.debit;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.accounttransactionservice.transaction.Transaction;
import com.ashish.accounttransactionservice.transaction.TransactionService;
import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitServiceTest {

    @Mock
    private PlayerAccountService playerAccountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private DebitService debitService;

    @Test
    void debitPlayerTestSuccess() {
        WalletRequest debitRequest = new WalletRequest(999L, 5.0, "EUR", 4567890L);
        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        Mockito.when(playerAccountService.getBalance(999L)).thenReturn(10.0);
        Mockito.when(transactionService.isTransactionExists(4567890L)).thenReturn(false);

        WalletResponse response = debitService.debit(debitRequest);

        assertEquals(999L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(4567890L, response.getTransactionId());

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(2)).getBalance(999L);
        verify(playerAccountService, times(1)).updatePlayerAccountBalance(999L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).isTransactionExists(4567890L);

    }

    @Test
    void debitPlayerTestWhenInSufficientFunds() {
        WalletRequest debitRequest = new WalletRequest(999L, 5.0, "EUR", 4567890L);
        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        Mockito.when(playerAccountService.getBalance(999L)).thenReturn(1.0);
        Mockito.when(transactionService.isTransactionExists(4567890L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> debitService.debit(debitRequest));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(1)).getBalance(999L);
        verify(transactionService, times(1)).isTransactionExists(4567890L);
        verifyNoMoreInteractions(playerAccountService, transactionService);

    }

    @Test
    void debitPlayerTestWhenDuplicateTransaction() {
        WalletRequest debitRequest = new WalletRequest(999L, 5.0, "EUR", 4567890L);
        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        Mockito.when(transactionService.isTransactionExists(4567890L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> debitService.debit(debitRequest));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(transactionService, times(1)).isTransactionExists(4567890L);
        verifyNoMoreInteractions(playerAccountService, transactionService);

    }

    @Test
    void debitPlayerTestWhenInvalidPlayer() {
        WalletRequest debitRequest = new WalletRequest(999L, 5.0, "EUR", 4567890L);
        Mockito.when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> debitService.debit(debitRequest));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verifyNoMoreInteractions(playerAccountService, transactionService);

    }

}