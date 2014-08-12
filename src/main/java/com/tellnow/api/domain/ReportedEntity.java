package com.tellnow.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.listener.ReportedEntityListener;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "reported_entities")
@EntityListeners(value = { ReportedEntityListener.class })
@ApiModel(value = "reporting object")
public class ReportedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ApiModelProperty(required = false, notes = "this is set based on logged user")
	private Long complainerId;

	private Long reportedUserId;

	private String reportedEntity;

	private Integer reportingType;

	@ApiModelProperty(required = false, notes = "this is set based on which service is called answer or question report")
	private Integer entityType;

	@Column(length = 1024)
	private String notes;

	public enum ReportingType {
		inappropriate(1), abuse(2), not_specified(3);

		private int value;

		ReportingType(int value) {
			this.value = value;
		}

		public static ReportingType getDefault() {
			return not_specified;
		}

		public int getValue() {
			return value;
		}

		public static ReportingType parse(int id) {
			ReportingType type = not_specified; // Default
			for (ReportingType item : ReportingType.values()) {
				if (item.getValue() == id) {
					type = item;
					break;
				}
			}
			return type;
		}
	}

	public enum EntityType {
		question(1), answer(2);

		private int value;

		EntityType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static EntityType parse(int id) {
			EntityType type = null; // Default
			for (EntityType item : EntityType.values()) {
				if (item.getValue() == id) {
					type = item;
					break;
				}
			}
			return type;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getComplainerId() {
		return complainerId;
	}

	public void setComplainerId(Long complainerId) {
		this.complainerId = complainerId;
	}

	public Long getReportedUserId() {
		return reportedUserId;
	}

	public void setReportedUserId(Long reportedUserId) {
		this.reportedUserId = reportedUserId;
	}

	public String getReportedEntity() {
		return reportedEntity;
	}

	public void setReportedEntity(String reportedEntity) {
		this.reportedEntity = reportedEntity;
	}

	public ReportingType getReportingType() {
		return this.reportingType != null ? ReportingType.parse(this.reportingType) : ReportingType.getDefault();
	}

	public void setReportingType(ReportingType reportingType) {
		this.reportingType = reportingType.getValue();
	}

	public EntityType getEntityType() {
		return this.entityType != null ? EntityType.parse(this.entityType) : null;
	}

	public void setEntityType(EntityType entityType) {
		if (entityType != null) {
			this.entityType = entityType.getValue();
		}

	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
