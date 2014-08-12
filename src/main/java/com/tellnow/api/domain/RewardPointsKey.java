package com.tellnow.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@SuppressWarnings("serial")
@Embeddable
public class RewardPointsKey implements Serializable {

	@Column(name = "profile_id", nullable = false)
	private Long profileId;

	@Column(name = "topic_id", nullable = false)
	private Long topicId;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
}
