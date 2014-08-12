package com.tellnow.api.exceptions.handling;

public enum GlobalErrorCodes {

	no_error(0), unauthorized(1), missing_phone_number(2), expired_code(3), internal_server_error(4), bad_request(5), not_allowed(6), not_found(7),
	exceeded_max_number_of_messages(8);
	public String ERORR_MESSAGE_PREFIX= "global.error.";
	private int errorCode;

	private GlobalErrorCodes(int code) {
		errorCode = code;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessageCode() {
		return ERORR_MESSAGE_PREFIX.concat(Integer.toString(errorCode));
	}

}
