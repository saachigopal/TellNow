package com.tellnow.api.Tropo;

public class TropoMessage {

	private String TOKEN = null;
	private String numberToDial = null;
	private String msg = null;

	public TropoMessage() {
	}

	public TropoMessage(String TOKEN, String numberToDial, String msg) {
		this.TOKEN = TOKEN;
		this.numberToDial = numberToDial;
		this.msg = msg;
	}

	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String TOKEN) {
		this.TOKEN = TOKEN;
	}

	public String getNumberToDial() {
		return numberToDial;
	}

	public void setNumberToDial(String numberToDial) {
		this.numberToDial = numberToDial;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
