package com.rabo.csp.exception;

/**
 * @author 739243
 *
 */
public class CSPException extends Exception {

	private static final long serialVersionUID = 5756238830272171170L;

	public CSPException(Throwable excption) {
		super(excption);
	}
	
	public CSPException(String message) {
		super(message);
	}

	public CSPException(String message, Throwable cause) {
		super(message, cause);
	}

}