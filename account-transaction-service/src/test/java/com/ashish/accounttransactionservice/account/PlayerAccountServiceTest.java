package com.ashish.accounttransactionservice.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerAccountServiceTest {

    @Mock
    private PlayerAccountRepository playerAccountRepository;
    @InjectMocks
    private PlayerAccountService playerAccountService;

    @Test
    void getBalanceTest() {
        PlayerAccount playerAccount = new PlayerAccount(999L, 200.0, "EUR");
        when(playerAccountRepository.getById(999L)).thenReturn(playerAccount);

        double response = playerAccountService.getBalance(999L);

        assertEquals(200.0, response);
        verify(playerAccountRepository, times(1)).getById(999L);
    }

    @Test
    void updatePlayerBalanceTest() {
        PlayerAccount playerAccount = new PlayerAccount(999L, 200.0, "EUR");
        when(playerAccountRepository.save(playerAccount)).thenReturn(playerAccount);

        PlayerAccount response = playerAccountService.updatePlayerAccountBalance(999L, 200.0, "EUR");

        assertEquals(playerAccount, response);
        verify(playerAccountRepository, times(1)).save(playerAccount);
    }

    @Test
    void accountExistTest() {
        when(playerAccountRepository.existsById(9999000L)).thenReturn(true);

        assertTrue(playerAccountService.isPlayerAccountExists(9999000L));
        verify(playerAccountRepository, Mockito.times(1)).existsById(9999000L);
    }

    @Test
    void getBalanceTestPlayerAccountServiceException() {
        when(playerAccountRepository.getById(999L)).thenThrow(new RuntimeException());

        assertThrows(PlayerAccountServiceException.class, () -> playerAccountService.getBalance(999L));

        verify(playerAccountRepository, times(1)).getById(999L);
    }

    @Test
    void updatePlayerBalanceTestPlayerAccountServiceException() {
        PlayerAccount playerAccount = new PlayerAccount(999L, 200.0, "EUR");
        when(playerAccountRepository.save(playerAccount)).thenThrow(new RuntimeException());

        assertThrows(PlayerAccountServiceException.class, () -> playerAccountService.updatePlayerAccountBalance(999L, 200.0, "EUR"));

        verify(playerAccountRepository, times(1)).save(playerAccount);
    }

    @Test
    void accountExistTestPlayerAccountServiceException() {
        when(playerAccountRepository.existsById(9999000L)).thenThrow(new RuntimeException());

        assertThrows(PlayerAccountServiceException.class, () -> playerAccountService.isPlayerAccountExists(9999000L));

        verify(playerAccountRepository, Mockito.times(1)).existsById(9999000L);
    }

}