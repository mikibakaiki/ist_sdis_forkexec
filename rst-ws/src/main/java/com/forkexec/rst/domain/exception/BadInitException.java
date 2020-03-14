package com.forkexec.rst.domain.exception;

/** Exception used to signal a problem with the initial parameters. */
public class BadInitException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadInitException() {
	}

	public BadInitException(String message) {
		super(message);
	}

}