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
@Table(name = "device")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String deviceToken;
	
	@Column(name = "last_used", nullable = false)
	private Boolean lastUsed = false; 
	
	@JsonIgnore
	@ManyToOne
	private TellnowProfile profile;

	public Device() {
	}

	public Device(String deviceToken) {
		this.deviceToken = deviceToken;
		this.lastUsed = false;
	}

	public Device(String deviceToken, TellnowProfile profile) {
		this.deviceToken = deviceToken;
		this.profile = profile;
		this.lastUsed = false;
	}

	public Device(Long id, String deviceToken, TellnowProfile profile) {
		this.id = id;
		this.deviceToken = deviceToken;
		this.profile = profile;
		this.lastUsed = false;
	}

	public Device(Long id, String deviceToken, TellnowProfile profile, Boolean lastUsed) {
		this.id = id;
		this.deviceToken = deviceToken;
		this.profile = profile;
		this.lastUsed = lastUsed;
	}

	public Device(Device device) {
		this.id = device.getId();
		this.deviceToken = device.getDeviceToken();
		this.profile = device.getProfile();
		this.lastUsed = device.getLastUsed();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Boolean getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Boolean lastUsed) {
		this.lastUsed = lastUsed;
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
			oldProfile.removeDevice(this);
		if (profile != null)
			profile.addDeviceCumulative(this);
	}

	private boolean sameAsFormer(TellnowProfile profile) {
		return this.profile == null ? profile == null : this.profile.equals(profile);
	}
}
