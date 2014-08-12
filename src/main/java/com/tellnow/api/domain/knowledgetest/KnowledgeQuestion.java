package com.tellnow.api.domain.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.Topic;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "knowledge_question")
public class KnowledgeQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;
	
	private String questionPublicId;
	
	private String questionText;
	
	private Date creationDate;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "knowledge_question_media", joinColumns = { @JoinColumn(name = "question_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "media_id", referencedColumnName = "id") })
	private Set<MediaFile> mediaFiles;// picture, sound, video

	@OneToMany(mappedBy="question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<KnowledgeAnswer> answers;

	@OneToMany(mappedBy="question")
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<KnowledgeResponse> responses;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="topicId", referencedColumnName="id")
	private Topic topic;

	public KnowledgeQuestion() {
	}

	public KnowledgeQuestion(String questionText) {
		this.questionPublicId = UUID.randomUUID().toString();
		this.questionText = questionText;
		this.creationDate = new Date();
	}

	public KnowledgeQuestion(String questionPublicId, String questionText) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = new Date();
	}
	
	public KnowledgeQuestion(Long id, String questionPublicId, String questionText, Date creationDate, Set<MediaFile> mediaFiles, Set<KnowledgeAnswer> answers, Set<KnowledgeResponse> responses, Topic topic) {
		this.id = id;
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
		this.mediaFiles = mediaFiles;
		this.answers = answers;
		this.responses = responses;
		this.topic = topic;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionPublicId() {
		return questionPublicId;
	}

	public void setQuestionPublicId(String questionPublicId) {
		this.questionPublicId = questionPublicId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
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
	
	public Set<KnowledgeAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<KnowledgeAnswer> answers) {
		this.answers = answers;
	}

	public Set<KnowledgeResponse> getResponses() {
		return responses;
	}

	public void setResponses(Set<KnowledgeResponse> responses) {
		this.responses = responses;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	@Transient
	public Set<KnowledgeAnswer> getGoodAnswers(){
		Set<KnowledgeAnswer> result = new HashSet<KnowledgeAnswer>();
		if(answers!=null){
			for(KnowledgeAnswer answer : answers){
				if(answer.getGood()){
					result.add(answer);
				}
			}
		}
		return result;
	}

	@Transient
	public Set<KnowledgeAnswer> getWrongAnswers(){
		Set<KnowledgeAnswer> result = new HashSet<KnowledgeAnswer>();
		if(answers!=null){
			for(KnowledgeAnswer answer : answers){
				if(!answer.getGood()){
					result.add(answer);
				}
			}
		}
		return result;
	}

	public void addResponse(KnowledgeResponse response){
		if(responses == null){
			responses = new HashSet<KnowledgeResponse>();
		}
		response.setQuestion(this);
		responses.add(response);
	}

	@Override
	public String toString() {
		return String.format("Question: %s\n" + " text: %d\n", this.questionPublicId, this.questionText);
	}	
}
