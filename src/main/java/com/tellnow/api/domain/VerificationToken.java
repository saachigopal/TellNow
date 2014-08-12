package com.tellnow.api.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "verification_token")
public class VerificationToken implements Serializable {

	private static final long serialVersionUID = 1409556559685436869L;

	private static final int DEFAULT_EXPIRY_TIME_IN_MINS = 60 * 24; // 24 hours

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(length = 36)
	private final String token;

	@JsonIgnore
	private Date expiryDate;

	@JsonIgnore
	private boolean verified;

	private String profileID;

	public VerificationToken() {

		this.token = UUID.randomUUID().toString();
		this.expiryDate = calculateExpiryDate(DEFAULT_EXPIRY_TIME_IN_MINS);
	}

	public VerificationToken(int expiryDate) {

		this.token = UUID.randomUUID().toString();
		this.expiryDate = calculateExpiryDate(expiryDate);
	}

	public VerificationToken(String token, int expiryDate) {

		this.token = token;
		this.expiryDate = calculateExpiryDate(expiryDate);
	}

	/**
	 * 
	 * @param token
	 */
	public VerificationToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public String getToken() {
		return token;
	}

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public void updateExpiryDate(Integer expiryInMinute) {
		Date d = this.calculateExpiryDate(expiryInMinute != null ? expiryInMinute : DEFAULT_EXPIRY_TIME_IN_MINS);
		this.expiryDate = d;
	}

	@Transient
	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		DateTime now = new DateTime();
		return now.plusMinutes(expiryTimeInMinutes).toDate();
	}

	@Transient
	public boolean hasExpired() {
		DateTime tokenDate = new DateTime(getExpiryDate());
		return tokenDate.isBeforeNow();
	}
}
