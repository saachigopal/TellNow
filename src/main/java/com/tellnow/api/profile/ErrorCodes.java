package com.tellnow.api.profile;

public enum ErrorCodes {

	missing_profile(1), already_exists(2), bad_input(3), unauthorized_operation(4);
	public String ERORR_MESSAGE_PREFIX = "profile.error.";
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
