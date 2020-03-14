package com.forkexec.pts.ws.cli;

/** 
 * 
 * Exception to be thrown when user doesn't have enough points on the account. 
 * 
 */
public class InvalidPointsFault_Exception extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPointsFault_Exception() {
		super();
	}

	public InvalidPointsFault_Exception(String message) {
		super(message);
	}

	public InvalidPointsFault_Exception(Throwable cause) {
		super(cause);
	}

	public InvalidPointsFault_Exception(String message, Throwable cause) {
		super(message, cause);
	}
}
