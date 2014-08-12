package com.tellnow.api.question;

public enum ErrorCodes {

	missing_question(1), save_error(2), delete_error(3), empty_page(4), duplicate_question(5), anonymous_question(6);
	public String ERORR_MESSAGE_PREFIX = "question.error.";
	private int errorCode;

	private ErrorCodes(int code) {
		errorCode = code;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessageCode() {
		return ERORR_MESSAGE_PREFIX.concat(Integer.toString(errorCode));
	}

}
