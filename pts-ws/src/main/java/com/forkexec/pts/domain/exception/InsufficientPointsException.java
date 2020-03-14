package com.forkexec.pts.domain.exception;

public class InsufficientPointsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsufficientPointsException() {
	}

	public InsufficientPointsException(String message) {
		super(message);
	}
}
