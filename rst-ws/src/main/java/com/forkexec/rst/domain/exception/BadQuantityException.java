package com.forkexec.rst.domain.exception;

/** Exception used to signal a problem with the menu quantity. */
public class BadQuantityException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadQuantityException() {
	}

	public BadQuantityException(String message) {
		super(message);
	}

}