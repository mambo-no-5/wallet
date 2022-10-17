package com.ashish.accounttransactionservice.account;

public class PlayerAccountServiceException extends RuntimeException {

    public PlayerAccountServiceException(String errorMessage) {
        super(errorMessage);
    }

    public PlayerAccountServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
