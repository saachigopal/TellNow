package com.tellnow.api.utils.knowledgetest;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeSimpleResponseUtil {
	
	private String questionId;
	private Set<String> answers;
	
	public KnowledgeSimpleResponseUtil() {
	}

	public KnowledgeSimpleResponseUtil(String questionId) {
		this.questionId = questionId;
		this.answers = new HashSet<String>();
	}

	public KnowledgeSimpleResponseUtil(String questionId, Set<String> answers) {
		this.questionId = questionId;
		this.answers = answers;
	}

	public KnowledgeSimpleResponseUtil(String questionId, String... answers) {
		this.questionId = questionId;
		this.answers = new HashSet<String>();
		if(answers!=null){
			for(String answer : answers){
				addResponse(answer);
			}
		}
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Set<String> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<String> answers) {
		this.answers = answers;
	}
	
	public void addResponse(String answer){
		if(answers == null){
			answers.add(answer);
		}
		answers.add(answer);
	}
}
