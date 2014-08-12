package com.tellnow.api.Tropo;

@SuppressWarnings("serial")
public class TropoException extends Exception {

	public TropoException(String message) {
		super(message);
	}

	public TropoException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
