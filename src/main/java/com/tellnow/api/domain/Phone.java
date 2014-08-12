package com.tellnow.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "phone")
public class Phone {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "phoneNumber", unique = true, nullable = false)
	private String phoneNumber;

	@JsonIgnore
	@ManyToOne
	private TellnowProfile profile;
	
	public Phone() {
	}
	
	public Phone(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Phone(Long id, String phoneNumber) {
		this.id = id;
		this.phoneNumber = phoneNumber;
	}

	public Phone(Phone phone) {
		this.id = phone.getId();
		this.phoneNumber = this.getPhoneNumber();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TellnowProfile getProfile() {
		return profile;
	}

	public void setProfile(TellnowProfile profile) {
		if (sameAsFormer(profile)) {
			return;
		}
		TellnowProfile oldProfile = this.profile;
		this.profile = profile;
		if (oldProfile != null)
			oldProfile.removePhone(this);
		if (profile != null)
			profile.addPhoneCumulative(this);
	}

	private boolean sameAsFormer(TellnowProfile profile) {
		return this.profile == null ? profile == null : this.profile.equals(profile);
	}
}
