package com.ashish.accounttransactionservice.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transactionId")
    private long transactionId;

    @Column(name = "playerId")
    private long playerId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transactionType")
    private TransactionType transactionType;

    @Column(name = "transactionTimestamp")
    private String transactionTimestamp;


}
