package com.tellnow.api.exceptions.handling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

public class ValidationMessages {

	/** List of field validation errors. */
	private List<FieldError> fieldErrors = new ArrayList<FieldError>();

	/** Gets the list of field validation errors. */
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	/**
	 * Adds a field validation error.
	 * 
	 * @param message
	 * @param field
	 */
	public void addFieldError(String objectName, String field, String message) {
		FieldError fieldError = new FieldError(objectName, field, message);
		fieldErrors.add(fieldError);
	}
}