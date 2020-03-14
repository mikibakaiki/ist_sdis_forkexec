package com.forkexec.rst.domain.exception;

/** Exception used to signal a problem with the menu quantity. */
public class InsufficientQuantityException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsufficientQuantityException() {
	}

	public InsufficientQuantityException(String message) {
		super(message);
	}

}