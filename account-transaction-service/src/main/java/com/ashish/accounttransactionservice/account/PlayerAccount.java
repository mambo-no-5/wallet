package com.ashish.accounttransactionservice.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playerAccounts")
public class PlayerAccount {

    @Id
    @Column(name = "playerId")
    private long playerId;

    @Column(name = "balance")
    private double balance;

    @Column(name = "currency")
    private String currency;

}
