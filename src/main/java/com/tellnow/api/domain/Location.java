package com.tellnow.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.spatial.Coordinates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "location")
public class Location implements Coordinates {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Double longitude;
	private Double latitude;

	private String humanReadableAddress;

	public Location() {
	}

	public Location(Double longitude, Double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Location(Double longitude, Double latitude, String humanReadableAddress) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.humanReadableAddress = humanReadableAddress;
	}

	public Location(Long id, Double longitude, Double latitude, String humanReadableAddress) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.humanReadableAddress = humanReadableAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getHumanReadableAddress() {
		return humanReadableAddress;
	}

	public void setHumanReadableAddress(String humanReadableAddress) {
		this.humanReadableAddress = humanReadableAddress;
	}

}
