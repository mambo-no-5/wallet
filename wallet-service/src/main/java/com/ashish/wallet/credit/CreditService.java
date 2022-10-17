package com.ashish.wallet.credit;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.accounttransactionservice.transaction.Transaction;
import com.ashish.accounttransactionservice.transaction.TransactionService;
import com.ashish.accounttransactionservice.transaction.TransactionType;
import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import com.ashish.wallet.exception.WalletException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreditService {

    private final PlayerAccountService playerAccountService;
    private final TransactionService transactionService;

    public CreditService(PlayerAccountService playerAccountService,
                         TransactionService transactionService) {
        this.playerAccountService = playerAccountService;
        this.transactionService = transactionService;
    }

    public WalletResponse credit(WalletRequest creditRequest) {

        long playerId = creditRequest.getPlayerId();
        double amount = creditRequest.getAmount();
        String currency = creditRequest.getCurrency();
        long transactionId = creditRequest.getTransactionId();

        if(transactionService.isTransactionExists(transactionId)) {
            throw new WalletException(String.format("[Transaction id=%s] is not unique.", transactionId));
        }

        Transaction transaction = new Transaction(transactionId, playerId, amount, TransactionType.CREDIT, Instant.now().toString());

        if (playerAccountService.isPlayerAccountExists(playerId)) {
            double updateBalance = playerAccountService.getBalance(playerId) + amount;
            playerAccountService.updatePlayerAccountBalance(playerId, updateBalance, currency);
            transactionService.saveTransaction(transaction);
        } else {
            playerAccountService.updatePlayerAccountBalance(playerId, amount, currency);
            transactionService.saveTransaction(transaction);
        }

        return new WalletResponse(playerId, playerAccountService.getBalance(playerId), transactionId);
    }

}
