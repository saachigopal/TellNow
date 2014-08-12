package com.tellnow.api.exceptions;

/**
 * Can be thrown when an error should be shown to the user with a specific http code
 * 
 * OBS: RestExceptions are not logged. If logging is needed, it should be done before the exception is being thrown.
 */
@SuppressWarnings("serial")
public class RestException extends RuntimeException {
	private String message;
	private int status;

	/**
	 * 
	 * @param message
	 *            Message to show the user
	 * @param status
	 *            One of HttpServletResponse
	 */
	public RestException(String message, int status) {
		this.message = message;
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
}
