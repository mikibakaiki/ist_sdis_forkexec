package com.forkexec.hub.domain.exception;

public class InvalidMenuInitException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidMenuInitException() {

    }
    public InvalidMenuInitException(String message) {
        super(message);
    }
}
