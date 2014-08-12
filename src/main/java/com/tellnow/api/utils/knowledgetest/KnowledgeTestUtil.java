package com.tellnow.api.utils.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.tellnow.api.domain.knowledgetest.KnowledgeResponse;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;

public class KnowledgeTestUtil {

	private String testId;
	
	private String profileId;
	
	private Date creationDate;

	private Date submitDate;
	
	private Set<KnowledgeResponseUtil> responses;

	private Set<KnowledgeQuestionUtil> questions;

	private Integer good;
	
	private Integer wrong;
	
	private Boolean submited;

	public KnowledgeTestUtil() {
	}

	public KnowledgeTestUtil(KnowledgeTest test) {
		this.testId = test.getTestId();
		this.profileId = test.getProfile().getProfileId();
		this.creationDate = test.getCreationDate();
		this.submitDate = test.getSubmitDate();
		if(test!=null && test.getResponses()!=null){
			for(KnowledgeResponse response : test.getResponses()){
				KnowledgeResponseUtil responseUtil = new KnowledgeResponseUtil(response);
				this.addResponse(responseUtil);
				KnowledgeQuestionUtil questionUtil = new KnowledgeQuestionUtil(response.getQuestion());
				this.addQuestion(questionUtil);
			}
		}
		this.good = test.getGood();
		this.wrong = test.getWrong();
		this.submited = test.getSubmited();
	}

	public KnowledgeTestUtil(String testId) {
		this.testId = testId;
		this.creationDate = new Date();
	}

	public KnowledgeTestUtil(String testId, String profileId) {
		this.testId = testId;
		this.profileId = profileId;
		this.creationDate = new Date();
	}

	public KnowledgeTestUtil(String testId, String profileId, Set<KnowledgeResponseUtil> responses, Set<KnowledgeQuestionUtil> questions, Integer good, Integer wrong) {
		this.testId = testId;
		this.profileId = profileId;
		this.creationDate = new Date();
		this.responses = responses;
		this.questions = questions;
		this.good = good;
		this.wrong = wrong;
	}

	public KnowledgeTestUtil(String testId, String profileId, Date creationDate, Date submitDate, Set<KnowledgeResponseUtil> responses, Set<KnowledgeQuestionUtil> questions, Integer good, Integer wrong) {
		super();
		this.testId = testId;
		this.profileId = profileId;
		this.creationDate = creationDate;
		this.submitDate = submitDate;
		this.responses = responses;
		this.questions = questions;
		this.good = good;
		this.wrong = wrong;
		this.submited = true;
	}

	public KnowledgeTestUtil(String testId, String profileId, Date creationDate, Date submitDate, Set<KnowledgeResponseUtil> responses, Set<KnowledgeQuestionUtil> questions, Integer good, Integer wrong, Boolean submited) {
		super();
		this.testId = testId;
		this.profileId = profileId;
		this.creationDate = creationDate;
		this.submitDate = submitDate;
		this.responses = responses;
		this.questions = questions;
		this.good = good;
		this.wrong = wrong;
		this.submited = submited;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Set<KnowledgeResponseUtil> getResponses() {
		return responses;
	}

	public void setResponses(Set<KnowledgeResponseUtil> responses) {
		this.responses = responses;
	}

	public Set<KnowledgeQuestionUtil> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<KnowledgeQuestionUtil> questions) {
		this.questions = questions;
	}

	public Integer getGood() {
		return good;
	}

	public void setGood(Integer good) {
		this.good = good;
	}

	public Integer getWrong() {
		return wrong;
	}

	public void setWrong(Integer wrong) {
		this.wrong = wrong;
	}

	public Boolean getSubmited() {
		return submited;
	}

	public void setSubmited(Boolean submited) {
		this.submited = submited;
	}
	
	public void addResponse(KnowledgeResponseUtil responseUtil){
		if(this.responses==null){
			this.responses = new HashSet<KnowledgeResponseUtil>();
		}
		this.responses.add(responseUtil);
	}

	public void addQuestion(KnowledgeQuestionUtil questionUtil){
		if(this.questions==null){
			this.questions = new HashSet<KnowledgeQuestionUtil>();
		}
		this.questions.add(questionUtil);
	}
	
}
