package com.forkexec.rst.domain.exception;

/** Exception used to signal a problem with the input text. */
public class BadTextException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadTextException() {
	}

	public BadTextException(String message) {
		super(message);
	}

}