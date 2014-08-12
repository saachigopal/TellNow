package com.tellnow.api.question;

public enum SortAnswersBy {

	id("id"), creation_date("creationDate");

	private String field;

	private SortAnswersBy(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
