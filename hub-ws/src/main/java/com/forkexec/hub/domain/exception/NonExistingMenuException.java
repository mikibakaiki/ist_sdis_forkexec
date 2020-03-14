package com.forkexec.hub.domain.exception;

public class NonExistingMenuException extends Exception {
    private static final long serialVersionUID = 1L;

    public NonExistingMenuException() {

    }
    public NonExistingMenuException(String message) {
        super(message);
    }
}
