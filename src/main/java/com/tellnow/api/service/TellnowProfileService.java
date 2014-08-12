package com.tellnow.api.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tellnow.api.domain.Location;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.TellnowProfileStat;
import com.tellnow.api.domain.Topic;

public interface TellnowProfileService {

	TellnowProfile getProfile(Long id);

	TellnowProfile getProfile(String profileId);

	TellnowProfile getProfileByUid(String uid);

	TellnowProfile update(TellnowProfile profile, boolean create);

	String delete(String profileId);
	
	String delete(Long id);

	String delete(TellnowProfile profile);

	/* profile stats */
	TellnowProfileStat getProfileStats(Long id);

	TellnowProfileStat getProfileStatsForUser(TellnowProfile profile);

	TellnowProfileStat getProfileStatsForUser(Long id);

	TellnowProfileStat getProfileStatsForUser(String profileId);

	TellnowProfileStat updateLocation(Location location);

	TellnowProfileStat updateLocation(TellnowProfile profile, Location location);

	TellnowProfileStat incQuestionCounter(Date date);

	TellnowProfileStat incQuestionCounter(TellnowProfile profile, Date date);

	TellnowProfileStat incAnswerCounter(Date date);

	TellnowProfileStat incAnswerCounter(TellnowProfile profile, Date date);

	TellnowProfileStat incRewardedAnswerCounter(Date date);

	TellnowProfileStat incRewardedAnswerCounter(TellnowProfile profile, Date date);

	List<TellnowProfile> getAllProfiles();

	Set<Long> getTopProfiles(String sortBy);

	Set<TellnowProfile> getProfiles(Set<Long> candidates);
	
	Double getAllRewardPoints(Long id);
	
	Double getAllRewardPoints(String profileId);
	
	Double getAllRewardPoints(TellnowProfile profile);

	Set<TellnowProfile> getProfilesForTopic(Topic topic, Set<Long> exclude);
}
