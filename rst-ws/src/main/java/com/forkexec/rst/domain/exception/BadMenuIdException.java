package com.forkexec.rst.domain.exception;

/** Exception used to signal a problem with the menu id. */
public class BadMenuIdException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadMenuIdException() {
	}

	public BadMenuIdException(String message) {
		super(message);
	}

}