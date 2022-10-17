package com.ashish.wallet.credit;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.accounttransactionservice.transaction.Transaction;
import com.ashish.accounttransactionservice.transaction.TransactionService;
import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private PlayerAccountService playerAccountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private CreditService creditService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void creditPlayerSuccessCaseExistingPlayer() {

        WalletRequest creditRequest = new WalletRequest(999L, 5, "EUR", 2345678L);

        when(transactionService.isTransactionExists(2345678L)).thenReturn(false);
        when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        when(playerAccountService.getBalance(999L)).thenReturn(5.0);


        WalletResponse response = creditService.credit(creditRequest);

        assertEquals(999L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(2345678L, response.getTransactionId());


        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(2)).getBalance(999L);
        verify(playerAccountService, times(1)).updatePlayerAccountBalance(999L, 10.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).isTransactionExists(2345678L);

    }

    @Test
    void creditPlayerSuccessCaseNewPlayer() {

        WalletRequest creditRequest = new WalletRequest(999L, 5, "EUR", 2345678L);

        when(transactionService.isTransactionExists(2345678L)).thenReturn(false);
        when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(false);
        when(playerAccountService.getBalance(999L)).thenReturn(5.0);

        WalletResponse response = creditService.credit(creditRequest);

        assertEquals(999L, response.getPlayerId());
        assertEquals(5.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());
        assertEquals(2345678L, response.getTransactionId());

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(1)).getBalance(999L);
        verify(playerAccountService, times(1)).updatePlayerAccountBalance(999L, 5.0, "EUR");
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(transactionService, times(1)).isTransactionExists(2345678L);

    }

    @Test
    void creditPlayerFailureDuplicateTransaction() {

        WalletRequest creditRequest = new WalletRequest(999L, 5, "EUR", 2345678L);

        when(transactionService.isTransactionExists(2345678L)).thenReturn(true);

        Assertions.assertThrows(WalletException.class, () -> creditService.credit(creditRequest));

        verify(transactionService, times(1)).isTransactionExists(2345678L);
        verifyNoMoreInteractions(playerAccountService, transactionService);

    }

}