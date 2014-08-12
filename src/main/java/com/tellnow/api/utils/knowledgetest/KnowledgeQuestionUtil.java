package com.tellnow.api.utils.knowledgetest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;

public class KnowledgeQuestionUtil {

	private String questionPublicId;
	
	private String questionText;
	
	private Date creationDate;
	
	private Set<MediaFile> mediaFiles;// picture, sound, video

	private Set<KnowledgeAnswerUtil> answers;

	private Long topic;

	public KnowledgeQuestionUtil() {
	}

	public KnowledgeQuestionUtil(KnowledgeQuestion question) {
		this.questionPublicId = question.getQuestionPublicId();
		this.questionText = question.getQuestionText();
		this.creationDate = question.getCreationDate();
		this.mediaFiles = question.getMediaFiles();
		if(question!=null && question.getAnswers()!=null){
			for(KnowledgeAnswer answer : question.getAnswers()){
				KnowledgeAnswerUtil answerUtil = new KnowledgeAnswerUtil(answer);
				this.addAnswer(answerUtil);
			}
		}
		this.topic = question.getTopic().getId();
	}

	public KnowledgeQuestionUtil(String questionPublicId) {
		this.questionPublicId = questionPublicId;
		this.creationDate = new Date();
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = new Date();
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Topic topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = new Date();
		this.topic = topic.getId();
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Long topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = new Date();
		this.topic = topic;
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Date creationDate) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Date creationDate, Topic topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
		this.topic = topic.getId();
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Date creationDate, Long topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
		this.topic = topic;
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Date creationDate, Set<MediaFile> mediaFiles, Set<KnowledgeAnswerUtil> answers, Topic topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
		this.mediaFiles = mediaFiles;
		this.answers = answers;
		this.topic = topic.getId();
	}

	public KnowledgeQuestionUtil(String questionPublicId, String questionText, Date creationDate, Set<MediaFile> mediaFiles, Set<KnowledgeAnswerUtil> answers, Long topic) {
		this.questionPublicId = questionPublicId;
		this.questionText = questionText;
		this.creationDate = creationDate;
		this.mediaFiles = mediaFiles;
		this.answers = answers;
		this.topic = topic;
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

	public Set<KnowledgeAnswerUtil> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<KnowledgeAnswerUtil> answers) {
		this.answers = answers;
	}

	public Long getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic.getId();
	}	

	public void setTopic(Long topic) {
		this.topic = topic;
	}	

	public void addAnswer(KnowledgeAnswerUtil answerUtil){
		if(this.answers==null){
			this.answers = new HashSet<KnowledgeAnswerUtil>();
		}
		this.answers.add(answerUtil);
	}
	
	public int getNumberOfAnswers(){
		if(answers==null){
			return 0;
		} else {
			return answers.size();
		}
	}
}
