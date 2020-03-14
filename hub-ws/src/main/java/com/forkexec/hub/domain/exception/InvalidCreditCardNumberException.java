package com.forkexec.hub.domain.exception;

public class InvalidCreditCardNumberException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCreditCardNumberException() {

    }
    public InvalidCreditCardNumberException(String message) {
        super(message);
    }
}
