package com.ashish.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericWalletRequest {

    @NotNull(message = "Mandatory field playerId is missing")
    private Long playerId;

}
