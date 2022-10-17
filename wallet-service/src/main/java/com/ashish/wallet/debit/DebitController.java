package com.ashish.wallet.debit;

import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebitController {

    private final DebitService debitService;
    @Autowired
    public DebitController(DebitService debitService) {
        this.debitService = debitService;
    }

    @PostMapping(value = "/v1/wallet/debit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse debit(@RequestBody WalletRequest debitRequest) {
        return debitService.debit(debitRequest);
    }

}
