package com.tellnow.api.domain.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "knowledge_response")
public class KnowledgeResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	private String publicId;

	private String text;

	private Date responseDate;

	@ManyToOne
	@JoinColumn(name="question_id", referencedColumnName="id")
	@NotFound(action = NotFoundAction.IGNORE)
	private KnowledgeQuestion question;
	
	@ManyToMany(mappedBy="responses")
	private Set<KnowledgeAnswer> answers;

	@JsonIgnore
	@ManyToOne
	private KnowledgeTest test;

	public KnowledgeResponse() {
	}

	public KnowledgeResponse(KnowledgeQuestion question) {
		this.publicId = UUID.randomUUID().toString();
		question.addResponse(this);
		this.question = question;
	}

	public KnowledgeResponse(KnowledgeQuestion question, KnowledgeTest test) {
		this.publicId = UUID.randomUUID().toString();
		this.question = question;
		this.test = test;
	}

	public KnowledgeResponse(Long id, String publicId, String text, Date responseDate, KnowledgeQuestion question, KnowledgeTest test) {
		this.id = id;
		this.publicId = publicId;
		this.text = text;
		this.responseDate = responseDate;
		this.question = question;
		this.test = test;
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

	public KnowledgeTest getTest() {
		return test;
	}

	public Set<KnowledgeAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<KnowledgeAnswer> answers) {
		this.answers = answers;
	}

	public void setTest(KnowledgeTest test) {
		this.test = test;
	}
	
	public void addAnswer(KnowledgeAnswer answer) {
		if(answers == null){
			answers = new HashSet<KnowledgeAnswer>();
		}
		answers.add(answer);
		if((answer!=null && answer.getResponses()!=null && !answer.getResponses().contains(this))
				|| (answer!=null && answer.getResponses()==null)){
			answer.addResponse(this);
		}
	}
	
	@Override
	public String toString() {
		return String.format("Response: %s\n" + " text: %d\n", this.publicId);
	}
}

