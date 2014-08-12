package com.tellnow.api.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "profile_stat")
public class TellnowProfileStat {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "query_count", columnDefinition = "INT(10) default '0'")
	private Long queryCount;

	@Column(name = "response_count", columnDefinition = "INT(10) default '0'")
	private Long responseCount;

	@Column(name = "last_query_date")
	private Date timeofLastQuery;
	@Column(name = "last_response_date")
	private Date timeofLastResponse;

	@Column(name = "rewareded_response_count", columnDefinition = "INT(10) default '0'")
	private Long rewardedResponseCount;

	@Column(name = "last_rewarded_response_date")
	private Date timeofLastRewardedResponse;

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "last_location", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Location lastKnownLocation;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "profile", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private TellnowProfile profile;

	public TellnowProfileStat() {
	}

	public TellnowProfileStat(Long id, Long queryCount, Long responseCount, Date timeofLastQuery, Date timeofLastResponse, Long rewardedResponseCount, Date timeofLastRewardedResponse, Location lastKnownLocation, TellnowProfile profile) {
		this.id = id;
		this.queryCount = queryCount;
		this.responseCount = responseCount;
		this.timeofLastQuery = timeofLastQuery;
		this.timeofLastResponse = timeofLastResponse;
		this.rewardedResponseCount = rewardedResponseCount;
		this.timeofLastRewardedResponse = timeofLastRewardedResponse;
		this.lastKnownLocation = lastKnownLocation;
		this.profile = profile;
	}

	public TellnowProfileStat(Long queryCount, Long responseCount, Date timeofLastQuery, Date timeofLastResponse, Long rewardedResponseCount, Date timeofLastRewardedResponse, Location lastKnownLocation, TellnowProfile profile) {
		this.queryCount = queryCount;
		this.responseCount = responseCount;
		this.timeofLastQuery = timeofLastQuery;
		this.timeofLastResponse = timeofLastResponse;
		this.rewardedResponseCount = rewardedResponseCount;
		this.timeofLastRewardedResponse = timeofLastRewardedResponse;
		this.lastKnownLocation = lastKnownLocation;
		this.profile = profile;
	}

	public TellnowProfileStat(Long queryCount, Long responseCount, Date timeofLastQuery, Date timeofLastResponse, Long rewardedResponseCount, Date timeofLastRewardedResponse, Location lastKnownLocation) {
		this.queryCount = queryCount;
		this.responseCount = responseCount;
		this.timeofLastQuery = timeofLastQuery;
		this.timeofLastResponse = timeofLastResponse;
		this.rewardedResponseCount = rewardedResponseCount;
		this.timeofLastRewardedResponse = timeofLastRewardedResponse;
		this.lastKnownLocation = lastKnownLocation;
	}

	public TellnowProfileStat(Long queryCount, Long responseCount, Date timeofLastQuery, Date timeofLastResponse, Long rewardedResponseCount, Date timeofLastRewardedResponse) {
		this.queryCount = queryCount;
		this.responseCount = responseCount;
		this.timeofLastQuery = timeofLastQuery;
		this.timeofLastResponse = timeofLastResponse;
		this.rewardedResponseCount = rewardedResponseCount;
		this.timeofLastRewardedResponse = timeofLastRewardedResponse;
	}

	public TellnowProfileStat(Long queryCount, Long responseCount, Date timeofLastQuery, Date timeofLastResponse, Long rewardedResponseCount) {
		this.queryCount = queryCount;
		this.responseCount = responseCount;
		this.timeofLastQuery = timeofLastQuery;
		this.timeofLastResponse = timeofLastResponse;
		this.rewardedResponseCount = rewardedResponseCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(Long queryCount) {
		this.queryCount = queryCount;
	}

	public Long getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(Long responseCount) {
		this.responseCount = responseCount;
	}

	public Date getTimeofLastQuery() {
		return timeofLastQuery;
	}

	public void setTimeofLastQuery(Date timeofLastQuery) {
		this.timeofLastQuery = timeofLastQuery;
	}

	public Date getTimeofLastResponse() {
		return timeofLastResponse;
	}

	public void setTimeofLastResponse(Date timeofLastResponse) {
		this.timeofLastResponse = timeofLastResponse;
	}

	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}

	public void setLastKnownLocation(Location lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}

	public TellnowProfile getProfile() {
		return profile;
	}

	public void setProfile(TellnowProfile profile) {
		this.profile = profile;
	}

	public Long getRewardedResponseCount() {
		return rewardedResponseCount;
	}

	public void setRewardedResponseCount(Long rewardedResponseCount) {
		this.rewardedResponseCount = rewardedResponseCount;
	}

	public Date getTimeofLastRewardedResponse() {
		return timeofLastRewardedResponse;
	}

	public void setTimeofLastRewardedResponse(Date timeofLastRewardedResponse) {
		this.timeofLastRewardedResponse = timeofLastRewardedResponse;
	}
}
