package com.ashish.wallet.debit;

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
public class DebitService {

    private final PlayerAccountService playerAccountService;
    private final TransactionService transactionService;

    public DebitService(PlayerAccountService playerAccountService,
                        TransactionService transactionService) {
        this.playerAccountService = playerAccountService;
        this.transactionService = transactionService;
    }

    public WalletResponse debit(WalletRequest debitRequest) {

        Long playerId = debitRequest.getPlayerId();
        double debitAmount = debitRequest.getAmount();
        String currency = debitRequest.getCurrency();
        long transactionId = debitRequest.getTransactionId();

        validateDebitRequest(playerId, debitAmount, transactionId);

        double updatedBalance = playerAccountService.getBalance(playerId) - debitAmount;

        playerAccountService.updatePlayerAccountBalance(playerId, updatedBalance, currency);

        transactionService.saveTransaction(new Transaction(transactionId, playerId, debitAmount,
                TransactionType.DEBIT, Instant.now().toString()));

        return new WalletResponse(playerId, updatedBalance, transactionId);

    }

    private void validateDebitRequest(Long playerId, double debitAmount, long transactionId) {

        if (!playerAccountService.isPlayerAccountExists(playerId)) {
            throw new WalletException(String.format("Invalid [player id=%s], player account doesn't exists.", playerId));
        }

        if(transactionService.isTransactionExists(transactionId)) {
            throw new WalletException(String.format("[Transaction id=%s] is not unique.", transactionId));
        }

        double balance = playerAccountService.getBalance(playerId);
        if (!((balance - debitAmount) >= 0)) {
            throw new WalletException("Transaction declined. player don't have sufficient funds to play.");
        }

    }

}
