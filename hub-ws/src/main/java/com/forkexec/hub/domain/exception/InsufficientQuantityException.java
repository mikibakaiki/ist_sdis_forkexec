package com.forkexec.hub.domain.exception;

public class InsufficientQuantityException extends Exception {
    private static final long serialVersionUID = 1L;

    public InsufficientQuantityException() {

    }
    public InsufficientQuantityException(String message) {
        super(message);
    }
}
