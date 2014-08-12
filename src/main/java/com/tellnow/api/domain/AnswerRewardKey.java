package com.tellnow.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class AnswerRewardKey implements Serializable {

	@Column(name = "question_id", nullable = false)
	private Long questionId;

	@Column(name = "answer_id", nullable = false)
	private Long answerId;

	@Column(name = "topic_id", nullable = false)
	private Long topicId;

	@Column(name = "profile_id", nullable = false)
	private Long profileId;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
}