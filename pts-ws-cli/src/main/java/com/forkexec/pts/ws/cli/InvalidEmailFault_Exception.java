package com.forkexec.pts.ws.cli;

/** 
 * 
 * Exception to be thrown when email is on the wrong format. 
 * 
 */
public class InvalidEmailFault_Exception extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidEmailFault_Exception() {
		super();
	}

	public InvalidEmailFault_Exception(String message) {
		super(message);
	}

	public InvalidEmailFault_Exception(Throwable cause) {
		super(cause);
	}

	public InvalidEmailFault_Exception(String message, Throwable cause) {
		super(message, cause);
	}
}