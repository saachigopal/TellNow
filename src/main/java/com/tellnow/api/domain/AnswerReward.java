package com.tellnow.api.domain;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.listener.PushFeedbackNotificationsListener;

@JsonInclude(Include.NON_NULL)
@Entity
@Indexed
@Table(name = "answer_reward")
@EntityListeners(value = {PushFeedbackNotificationsListener.class })
public class AnswerReward {

	private static final String SEPARATOR = "_";

	@EmbeddedId
	@IndexedEmbedded
	private AnswerRewardKey id;

	@Field(store = Store.YES)
	private Date created;

	@Field(store = Store.YES)
	@NumericField
	private Double value;

	public AnswerRewardKey getId() {
		return id;
	}

	public void setId(AnswerRewardKey id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@DocumentId
	@Transient
	public String getDocumentId() {
		StringBuilder sb = new StringBuilder();
		sb.append(getId().getAnswerId());
		sb.append(SEPARATOR);
		sb.append(getId().getQuestionId());
		sb.append(SEPARATOR);
		sb.append(getId().getTopicId());
		sb.append(SEPARATOR);
		sb.append(getId().getProfileId());
		return sb.toString();
	}
}
