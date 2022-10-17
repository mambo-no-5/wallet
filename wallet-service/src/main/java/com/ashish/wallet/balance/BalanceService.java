package com.ashish.wallet.balance;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final PlayerAccountService playerAccountService;

    public BalanceService(PlayerAccountService playerAccountService) {
        this.playerAccountService = playerAccountService;
    }

    public WalletResponse playerBalance(Long playerId) {

        if (!playerAccountService.isPlayerAccountExists(playerId)) {
            throw new WalletException("Invalid player id, player account doesn't exists.");
        }

        double balance = playerAccountService.getBalance(playerId);
        return new WalletResponse(playerId, balance);

    }

}
