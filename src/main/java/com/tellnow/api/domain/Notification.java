package com.tellnow.api.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tellnow.api.utils.TellNowUtils;

@Entity
@Table(name = "notification")
public class Notification {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne
	private TellnowProfile profile;

	@JsonIgnore
	private Date date;
	
	@JsonIgnore
	private String dateAsString;
	
	@JsonIgnore
	private String deviceToken;
	
	private String message;
	
	private String text;
	
	@JsonIgnore
	private String status;

	@JsonProperty("id")
	private String notificationId;
	
	@JsonIgnore
	private boolean visible;
	
	public Notification() {
	}

	public Notification(TellnowProfile profile, Date date) {
		this.profile = profile;
		this.date = date;
		this.dateAsString = date.toGMTString();
		this.notificationId = UUID.randomUUID().toString();
		this.visible = true;
	}

	@SuppressWarnings("deprecation")
	public Notification(TellnowProfile profile, Date date, String payload) {
		this.profile = profile;
		this.date = date;
		this.dateAsString = date.toGMTString();
		this.message = TellNowUtils.getPayloadMessage(payload);
		this.notificationId = UUID.randomUUID().toString();
		this.visible = true;
	}

	@SuppressWarnings("deprecation")
	public Notification(TellnowProfile profile, Date date, String deviceToken, String payload) {
		this.profile = profile;
		this.date = date;
		this.dateAsString = date.toGMTString();
		this.deviceToken = deviceToken;
		this.message = TellNowUtils.getPayloadMessage(payload);
		this.notificationId = UUID.randomUUID().toString();
		this.visible = true;
	}

	@SuppressWarnings("deprecation")
	public Notification(TellnowProfile profile, Date date, String deviceToken, String payload, boolean visible) {
		this.profile = profile;
		this.date = date;
		this.dateAsString = date.toGMTString();
		this.deviceToken = deviceToken;
		this.message = TellNowUtils.getPayloadMessage(payload);
		this.notificationId = UUID.randomUUID().toString();
		this.visible = visible;
	}

	@SuppressWarnings("deprecation")
	public Notification(TellnowProfile profile, Long id, Date date, String deviceToken, String payload) {
		this.profile = profile;
		this.id = id;
		this.date = date;
		this.dateAsString = date.toGMTString();
		this.deviceToken = deviceToken;
		this.message = TellNowUtils.getPayloadMessage(payload);
		this.notificationId = UUID.randomUUID().toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateAsString() {
		return dateAsString;
	}

	public void setDateAsString(String dateAsString) {
		this.dateAsString = dateAsString;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TellnowProfile getProfile() {
		return profile;
	}

	public void setProfile(TellnowProfile profile) {
		this.profile = profile;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
