package com.tellnow.api.Tropo;

public class TropoResponse {

	private boolean success;
	private String token;
	private String id;
	private String reason;

	public TropoResponse() {
	}

	public TropoResponse(boolean success, String token) {
		this.success = success;
		this.token = token;
	}

	public TropoResponse(boolean success, String token, String id, String reason) {
		this.success = success;
		this.token = token;
		this.reason = id;
		this.reason = reason;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
