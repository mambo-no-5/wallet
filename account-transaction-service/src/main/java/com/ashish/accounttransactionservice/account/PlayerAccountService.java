package com.ashish.accounttransactionservice.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerAccountService {

    private final PlayerAccountRepository playerAccountRepository;

    public PlayerAccountService(PlayerAccountRepository playerAccountRepository) {
        this.playerAccountRepository = playerAccountRepository;
    }

    public double getBalance(long playerId) {
        try {
            PlayerAccount playerAccount = playerAccountRepository.getById(playerId);
            return playerAccount.getBalance();
        } catch (Exception e) {
            throw new PlayerAccountServiceException("Exception occurred while fetching player account balance", e);
        }
    }

    @Transactional
    public PlayerAccount updatePlayerAccountBalance(long playerId, double balance, String currency) {
        try {
            return playerAccountRepository.save(new PlayerAccount(playerId, balance, currency));

        } catch (Exception e) {
            throw new PlayerAccountServiceException("Exception occurred while updating player balance", e);
        }
    }

    public boolean isPlayerAccountExists(long playerId) {
        try {
            return playerAccountRepository.existsById(playerId);
        } catch (Exception e) {
            throw new PlayerAccountServiceException("Exception occurred while checking player account", e);
        }
    }

}
