package com.tellnow.api.utils.knowledgetest;

import java.util.Date;
import java.util.Set;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;

public class KnowledgeAnswerUtil {

	private String publicId;

	private String text;

	private Date creationDate;
	
	private boolean good;

	private KnowledgeQuestionUtil question;

	private Set<MediaFile> mediaFiles;// picture, sound, video

	public KnowledgeAnswerUtil() {
	}

	public KnowledgeAnswerUtil(KnowledgeAnswer answer) {
		this.publicId = answer.getPublicId();
		this.text = answer.getText();
		this.creationDate = answer.getCreationDate();
		this.good = answer.getGood();
		this.mediaFiles = answer.getMediaFiles();
	}

	public KnowledgeAnswerUtil(String publicId) {
		this.publicId = publicId;
		this.creationDate = new Date();
		this.good = false;
	}

	public KnowledgeAnswerUtil(String publicId, String text) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = new Date();
		this.good = false;
	}

	public KnowledgeAnswerUtil(String publicId, String text, boolean good) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = new Date();
		this.good = good;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = false;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate, boolean good) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = good;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate, KnowledgeQuestionUtil question) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = false;
		this.question = question;
	}

	public KnowledgeAnswerUtil(String publicId, String text, boolean good, KnowledgeQuestionUtil question) {
		this.publicId = publicId;
		this.text = text;
		this.good = good;
		this.creationDate = new Date();
		this.question = question;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate, boolean good, KnowledgeQuestionUtil question) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = good;
		this.question = question;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate, KnowledgeQuestionUtil question, Set<MediaFile> mediaFiles) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = false;
		this.question = question;
		this.mediaFiles = mediaFiles;
	}

	public KnowledgeAnswerUtil(String publicId, String text, Date creationDate, boolean good, KnowledgeQuestionUtil question, Set<MediaFile> mediaFiles) {
		this.publicId = publicId;
		this.text = text;
		this.creationDate = creationDate;
		this.good = good;
		this.question = question;
		this.mediaFiles = mediaFiles;
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

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public KnowledgeQuestionUtil getQuestion() {
		return question;
	}

	public void setQuestion(KnowledgeQuestionUtil question) {
		this.question = question;
	}

	public Set<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(Set<MediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}
}
