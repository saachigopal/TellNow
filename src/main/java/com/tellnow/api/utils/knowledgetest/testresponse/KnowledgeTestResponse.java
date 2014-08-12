package com.tellnow.api.utils.knowledgetest.testresponse;

import java.util.Set;

public class KnowledgeTestResponse {
	
	private String questionId;
	private Set<String> correctAnswers;

	public KnowledgeTestResponse() {
	}

	public KnowledgeTestResponse(String questionId, Set<String> correctAnswers) {
		this.questionId = questionId;
		this.correctAnswers = correctAnswers;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Set<String> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Set<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
}
