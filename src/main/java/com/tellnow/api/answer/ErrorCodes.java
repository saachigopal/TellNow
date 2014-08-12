package com.tellnow.api.answer;

public enum ErrorCodes {

	missing_question(1), save_error(2), delete_error(3), empty_page(4), expired_question(5), answer_limit(6), duplicate_answer(7);
	public String ERORR_MESSAGE_PREFIX = "answer.error.";
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
