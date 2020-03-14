package com.forkexec.hub.domain.exception;


/** Exception used to signal a problem with the amount of credit
 *  to add to a given client. */

public class InvalidCreditAmountException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCreditAmountException() {

    }
    public InvalidCreditAmountException(String message) {
        super(message);
    }

}
