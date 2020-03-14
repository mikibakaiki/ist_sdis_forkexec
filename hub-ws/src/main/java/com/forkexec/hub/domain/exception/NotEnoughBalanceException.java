package com.forkexec.hub.domain.exception;

public class NotEnoughBalanceException extends Exception {
    private static final long serialVersionUID = 1L;

    public NotEnoughBalanceException() {

    }
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}

