package com.ashish.wallet.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class WalletRequest extends GenericWalletRequest {

    private double amount;
    private String currency;
    @NotNull(message = "Mandatory field, transactionId is missing")
    private Long transactionId;

    public WalletRequest(Long playerId, double amount, String currency, Long transactionId) {
        super(playerId);
        this.amount = amount;
        this.currency = currency;
        this.transactionId = transactionId;
    }
}
