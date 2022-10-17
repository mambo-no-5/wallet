package com.ashish.wallet.history;

import com.ashish.accounttransactionservice.account.PlayerAccountService;
import com.ashish.accounttransactionservice.transaction.TransactionService;
import com.ashish.wallet.model.TransactionHistoryResponse;
import com.ashish.wallet.exception.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class HistoryController {

    private final PlayerAccountService playerAccountService;
    private final TransactionService transactionService;
    @Autowired
    public HistoryController(PlayerAccountService playerAccountService, TransactionService transactionService) {
        this.playerAccountService = playerAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/v1/wallet/history.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionHistoryResponse playerHistory(@RequestParam long playerId) {

        if (!playerAccountService.isPlayerAccountExists(playerId)) {
            throw new WalletException(String.format("Invalid [player id=%s], player account doesn't exists.", playerId));
        }

        return new TransactionHistoryResponse(transactionService.getTransactions(playerId));

    }

}
