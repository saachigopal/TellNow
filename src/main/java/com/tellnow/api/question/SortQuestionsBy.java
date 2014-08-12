package com.tellnow.api.question;

public enum SortQuestionsBy {

	id("id"), creation_date("creationDate");

	private String field;

	private SortQuestionsBy(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
