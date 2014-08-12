package com.tellnow.api;

import com.tellnow.api.exceptions.handling.GlobalErrorCodes;

public class TellnowResponse {

	private TellnowError error;

	private Object message;
	
	private Double rewardPoints;

	public TellnowResponse() {
		this.error = new TellnowError(GlobalErrorCodes.no_error.getErrorCode(), "");
	}

	public TellnowResponse(TellnowError error) {
		this.error = error;
	}

	public TellnowResponse(Object message) {
		this.error = new TellnowError(GlobalErrorCodes.no_error.getErrorCode(), "");
		this.message = message;
	}

	public TellnowResponse(TellnowError error, Object message) {
		this.error = error;
		this.message = message;
	}

	public TellnowError getError() {
		return error;
	}

	public void setError(TellnowError error) {
		this.error = error;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public Double getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
}
