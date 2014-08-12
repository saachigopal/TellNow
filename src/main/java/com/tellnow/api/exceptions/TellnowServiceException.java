package com.tellnow.api.exceptions;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.tellnow.api.TellnowError;

@SuppressWarnings("serial")
public class TellnowServiceException extends Exception {

	private final int errorCode;

	private final String errorMessageCode;

	@Autowired
	private MessageSource messages;

	public TellnowServiceException(int errorCode, String errorMessageCode, Exception e) {
		super(e);
		this.errorCode = errorCode;
		this.errorMessageCode = errorMessageCode;
	}

	public TellnowError getTellnowError() {
		return getLocalizedTellnowError(Locale.getDefault());
	}

	public TellnowError getLocalizedTellnowError(Locale locale) {
		return new TellnowError(errorCode, messages.getMessage(errorMessageCode, null, locale));
	}

}
