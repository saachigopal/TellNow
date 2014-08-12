package com.tellnow.api.domain.knowledgetest;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.domain.TellnowProfile;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "knowledge_test")
public class KnowledgeTest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(name="test_id")
	private String testId;
	
	private Date creationDate;

	private Date submitDate;

	@OneToOne
	@JoinColumn(name="profile_id", referencedColumnName="id")
	private TellnowProfile profile;

	@OneToMany(mappedBy="test")
	private Set<KnowledgeResponse> responses;

	@ManyToMany(mappedBy="tests")
	private Set<KnowledgeAnswer> answers;

	private Integer good;
	
	private Integer wrong;
	
	private Boolean submited;
	
	public KnowledgeTest() {
	}

	public KnowledgeTest(Long id) {
		this.id = id;
		this.testId = UUID.randomUUID().toString();
		this.creationDate = new Date();
	}

	public KnowledgeTest(TellnowProfile profile) {
		this.testId = UUID.randomUUID().toString();
		this.profile = profile;
		this.creationDate = new Date();
	}

	public KnowledgeTest(String testId, TellnowProfile profile) {
		this.testId = testId;
		this.profile = profile;
		this.creationDate = new Date();
	}
	
	public KnowledgeTest(String testId, TellnowProfile profile, Date creationDate) {
		this.testId = testId;
		this.profile = profile;
		this.creationDate = creationDate;
	}

	public KnowledgeTest(Long id, String testId, TellnowProfile profile) {
		this.id = id;
		this.testId = testId;
		this.profile = profile;
		this.creationDate = new Date();
	}

	public KnowledgeTest(Long id, TellnowProfile profile, Set<KnowledgeResponse> responses) {
		this.id = id;
		this.testId = UUID.randomUUID().toString();
		this.profile = profile;
		this.responses = responses;
		this.creationDate = new Date();
	}

	public KnowledgeTest(Long id, String testId, TellnowProfile profile, Set<KnowledgeResponse> responses) {
		this.id = id;
		this.testId = testId;
		this.profile = profile;
		this.responses = responses;
		this.creationDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
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

	public TellnowProfile getProfile() {
		return profile;
	}

	public void setProfile(TellnowProfile profile) {
		this.profile = profile;
	}

	public Collection<KnowledgeResponse> getResponses() {
		return responses;
	}

	public void setResponses(Set<KnowledgeResponse> responses) {
		this.responses = responses;
	}
	
	public Set<KnowledgeAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<KnowledgeAnswer> answers) {
		this.answers = answers;
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

	public void addResponse(KnowledgeResponse response){
		if(responses==null){
			responses = new HashSet<KnowledgeResponse>();
		}
		response.setTest(this);
		responses.add(response);
	}
	
	public void addAnswer(KnowledgeAnswer answer){
		if(answers==null){
			answers = new HashSet<KnowledgeAnswer>();
		}
		answers.add(answer);
		if((answer!=null && answer.getTests()!=null && !answer.getTests().contains(this))
				|| (answer!=null && answer.getTests()==null)){
			answer.addTest(this);
		}
	}
	
	public boolean conainsResponseOfQuestion(String questionPublicId){ 
		if(responses==null || responses.isEmpty()){
			return false;
		} else {
			for(KnowledgeResponse response : responses){
				if(response.getQuestion().getQuestionPublicId().equals(questionPublicId)){
					return true;
				}
			}
		}
		return false;
	}
}
