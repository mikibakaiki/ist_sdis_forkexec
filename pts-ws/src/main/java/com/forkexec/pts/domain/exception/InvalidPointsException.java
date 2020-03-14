package com.forkexec.pts.domain.exception;

public class InvalidPointsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidPointsException() {
	}

	public InvalidPointsException(String message) {
		super(message);
	}
}
