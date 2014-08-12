package com.tellnow.api.exceptions;

@SuppressWarnings("serial")
public class InvalidDeviceException extends Exception {
	public InvalidDeviceException(String message){
		super(message);
	}
}
