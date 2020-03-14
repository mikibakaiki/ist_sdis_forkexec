package com.forkexec.hub.domain.exception;

public class InvalidStartPointsException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidStartPointsException() {

    }
    public InvalidStartPointsException(String message) {
        super(message);
    }
}
