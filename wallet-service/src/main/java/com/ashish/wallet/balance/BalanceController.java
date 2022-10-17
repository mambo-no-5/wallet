package com.ashish.wallet.balance;

import com.ashish.wallet.model.GenericWalletRequest;
import com.ashish.wallet.model.WalletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping(value = "/v1/wallet/balance.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse getBalance(@RequestBody @Valid GenericWalletRequest balanceRequest) {
        return balanceService.playerBalance(balanceRequest.getPlayerId());
    }

}
