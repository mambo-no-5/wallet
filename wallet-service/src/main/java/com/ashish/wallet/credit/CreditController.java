package com.ashish.wallet.credit;

import com.ashish.wallet.model.WalletRequest;
import com.ashish.wallet.model.WalletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CreditController {

    private final CreditService creditService;
    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping(value = "/v1/wallet/credit.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse credit(@RequestBody @Valid WalletRequest creditRequest) {
        return creditService.credit(creditRequest);
    }

}
