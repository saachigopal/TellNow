package com.tellnow.api.utils.knowledgetest.testresponse;

import java.util.Set;

public class KnowledgeTestSubmit {

	private String testId;
	private Set<KnowledgeTestResponse> responses;
	
	public KnowledgeTestSubmit() {
	}
	
	public KnowledgeTestSubmit(String testId, Set<KnowledgeTestResponse> responses) {
		this.testId = testId;
		this.responses = responses;
	}
	
	public String getTestId() {
		return testId;
	}
	
	public void setTestId(String testId) {
		this.testId = testId;
	}
	
	public Set<KnowledgeTestResponse> getResponses() {
		return responses;
	}
	
	public void setResponses(Set<KnowledgeTestResponse> responses) {
		this.responses = responses;
	}
}
