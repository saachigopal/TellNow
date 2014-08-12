package com.tellnow.api.utils.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.domain.knowledgetest.KnowledgeResponse;

public class KnowledgeResponseUtil {
	
	private Long id;

	private String publicId;

	private String text;

	private Date responseDate;

	private KnowledgeQuestion question;
	
	private Set<KnowledgeAnswerUtil> answers;

	public KnowledgeResponseUtil() {
	}

	public KnowledgeResponseUtil(KnowledgeResponse response) {
		this.id = response.getId();
		this.publicId = response.getPublicId();
		this.text = response.getText();
		this.responseDate = response.getResponseDate();
		if(response!=null && response.getAnswers()!=null){
			for(KnowledgeAnswer answer : response.getAnswers()){
				KnowledgeAnswerUtil answerUtil = new KnowledgeAnswerUtil(answer);
				this.addAnswer(answerUtil);
			}
		}
	}

	public KnowledgeResponseUtil(String text) {
		this.publicId = UUID.randomUUID().toString();
		this.text = text;
		this.responseDate = new Date();
	}

	public KnowledgeResponseUtil(String publicId, String text) {
		this.publicId = publicId;
		this.text = text;
		this.responseDate = new Date();
	}

	public KnowledgeResponseUtil(String text, KnowledgeQuestion question) {
		this.publicId = UUID.randomUUID().toString();
		this.text = text;
		this.responseDate = new Date();
		this.question = question;
	}

	public KnowledgeResponseUtil(String text, KnowledgeQuestion question, Set<KnowledgeAnswerUtil> answers) {
		this.publicId = UUID.randomUUID().toString();
		this.text = text;
		this.responseDate = new Date();
		this.question = question;
		this.answers = answers;
	}

	public KnowledgeResponseUtil(String text, Date responseDate) {
		this.publicId = UUID.randomUUID().toString();
		this.text = text;
		this.responseDate = responseDate;
	}

	public KnowledgeResponseUtil(String publicId, String text, Date responseDate) {
		this.publicId = publicId;
		this.text = text;
		this.responseDate = responseDate;
	}

	public KnowledgeResponseUtil(Long id, String publicId, String text, Date responseDate) {
		this.id = id;
		this.publicId = publicId;
		this.text = text;
		this.responseDate = responseDate;
	}

	public KnowledgeResponseUtil(Long id, String publicId, String text, Date responseDate, KnowledgeQuestion question) {
		this.id = id;
		this.publicId = publicId;
		this.text = text;
		this.responseDate = responseDate;
		this.question = question;
	}

	public KnowledgeResponseUtil(Long id, String publicId, String text, Date responseDate, KnowledgeQuestion question, Set<KnowledgeAnswerUtil> answers) {
		this.id = id;
		this.publicId = publicId;
		this.text = text;
		this.responseDate = responseDate;
		this.question = question;
		this.answers = answers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public KnowledgeQuestion getQuestion() {
		return question;
	}

	public void setQuestion(KnowledgeQuestion question) {
		this.question = question;
	}

	public Set<KnowledgeAnswerUtil> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<KnowledgeAnswerUtil> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(KnowledgeAnswerUtil answerUtil){
		if(this.answers==null){
			this.answers = new HashSet<KnowledgeAnswerUtil>();
		}
		this.answers.add(answerUtil);
	}
}
