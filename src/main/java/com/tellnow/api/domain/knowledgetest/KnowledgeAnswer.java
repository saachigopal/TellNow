package com.tellnow.api.domain.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.domain.MediaFile;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "knowledge_answer")
public class KnowledgeAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	private String publicId;

	private String text;

	private Date creationDate;

	@ManyToOne
	@JoinColumn(name="question_id", referencedColumnName="")
	@NotFound(action = NotFoundAction.IGNORE)
	private KnowledgeQuestion question;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "knowledge_answer_media", 
		joinColumns = { @JoinColumn(name = "answer_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "media_id", referencedColumnName = "id") })
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<MediaFile> mediaFiles;// picture, sound, video

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "knowledge_answer_response", 
		joinColumns = { @JoinColumn(name = "answer_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "response_id", referencedColumnName = "id") })
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<KnowledgeResponse> responses;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "knowledge_answer_test", 
		joinColumns = { @JoinColumn(name = "answer_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "test_id", referencedColumnName = "id") })
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<KnowledgeTest> tests;

	@Column(name="good_or_bad")
	private Boolean good;
	
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(Set<MediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public KnowledgeQuestion getQuestion() {
		return question;
	}

	public void setQuestion(KnowledgeQuestion question) {
		this.question = question;
	}

	public Set<KnowledgeResponse> getResponses() {
		return responses;
	}

	public void setResponses(Set<KnowledgeResponse> responses) {
		this.responses = responses;
	}

	public Set<KnowledgeTest> getTests() {
		return tests;
	}

	public void setTests(Set<KnowledgeTest> tests) {
		this.tests = tests;
	}

	public Boolean getGood() {
		return good;
	}

	public void setGood(Boolean good) {
		this.good = good;
	}
	
	public void addResponse(KnowledgeResponse response){
		if(responses == null){
			responses = new HashSet<KnowledgeResponse>();
		}
		responses.add(response);
		if((response!=null && response.getAnswers()!=null && !response.getAnswers().contains(this))
				|| (response!=null && response.getAnswers()==null)){
			response.addAnswer(this);
		}
	}
	
	public void addTest(KnowledgeTest test){
		if(tests==null){
			tests = new HashSet<KnowledgeTest>();
		}
		tests.add(test);
		if((test!=null && test.getAnswers()!=null && !test.getAnswers().contains(this))
				|| (test!=null && test.getAnswers()==null)){
			test.addAnswer(this);
		}
	}
}
