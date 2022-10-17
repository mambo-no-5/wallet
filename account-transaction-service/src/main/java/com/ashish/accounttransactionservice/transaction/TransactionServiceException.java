package com.ashish.accounttransactionservice.transaction;

public class TransactionServiceException extends RuntimeException {

    public TransactionServiceException(String errorMessage) {
        super(errorMessage);
    }

    public TransactionServiceException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
