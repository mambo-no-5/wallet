package com.ashish.wallet.balance;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private PlayerAccountService playerAccountService;
    @InjectMocks
    private BalanceService balanceService;

    @Test
    void playerBalanceTest() {

        when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        when(playerAccountService.getBalance(999L)).thenReturn(400.0);

        WalletResponse response = balanceService.playerBalance(999L);

        assertEquals(999L, response.getPlayerId());
        assertEquals(400.0, response.getBalance());
        assertEquals("EUR", response.getCurrency());

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(1)).getBalance(999L);

    }

    @Test
    void playerBalanceTestInvalidPlayer() {

        when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(false);

        Assertions.assertThrows(WalletException.class, () -> balanceService.playerBalance(999L));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verifyNoMoreInteractions(playerAccountService);

    }

    @Test
    void playerBalanceTestUnExpectedError() {

        when(playerAccountService.isPlayerAccountExists(999L)).thenReturn(true);
        when(playerAccountService.getBalance(999L)).thenThrow(new RuntimeException("Unexpected error"));

        Assertions.assertThrows(RuntimeException.class, () -> balanceService.playerBalance(999L));

        verify(playerAccountService, times(1)).isPlayerAccountExists(999L);
        verify(playerAccountService, times(1)).getBalance(999L);

    }


}