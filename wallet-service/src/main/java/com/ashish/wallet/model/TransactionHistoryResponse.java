package com.ashish.wallet.model;

import com.ashish.accounttransactionservice.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
    List<Transaction> transactions;
}
