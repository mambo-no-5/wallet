package com.ashish.wallet.model;

import lombok.Data;

@Data
public class WalletResponse {

    private long playerId;
    private double balance;
    private long transactionId;
    private String currency;

    public WalletResponse(long playerId, double balance) {
        this.playerId = playerId;
        this.balance = balance;
        this.currency = "EUR";
    }

    public WalletResponse(long playerId, double balance, long transactionId) {
        this.playerId = playerId;
        this.balance = balance;
        this.transactionId = transactionId;
        this.currency = "EUR";
    }

}
