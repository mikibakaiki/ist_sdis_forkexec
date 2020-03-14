package com.forkexec.hub.domain.exception;

public class InvalidUserIdDomainException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidUserIdDomainException() {

    }
    public InvalidUserIdDomainException(String message) {
        super(message);
    }
}
