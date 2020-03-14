package com.forkexec.hub.domain.exception;

public class InvalidTextDescriptionException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTextDescriptionException() {

    }
    public InvalidTextDescriptionException(String message) {
        super(message);
    }
}
