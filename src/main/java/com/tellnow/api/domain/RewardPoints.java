package com.tellnow.api.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

@JsonInclude(Include.NON_NULL)
@Entity
@Indexed
@Table(name = "reward_points")
public class RewardPoints {

	private static final String SEPARATOR = "_";

	@EmbeddedId
	@IndexedEmbedded
	private RewardPointsKey id;

	@Field(store = Store.YES)
	@NumericField
	@Column(name = "points", columnDefinition = "double default '0'")
	private Double points;

	public RewardPointsKey getId() {
		return id;
	}

	public void setId(RewardPointsKey id) {
		this.id = id;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	@DocumentId
	@Transient
	public String getDocumentId() {
		StringBuilder sb = new StringBuilder();
		sb.append(getId().getProfileId());
		sb.append(SEPARATOR);
		sb.append(getId().getTopicId());
		return sb.toString();
	}
}
