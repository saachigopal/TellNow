package com.tellnow.api.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.TellnowProfileStat;

public interface ProfileStatsRepository extends JpaRepository<TellnowProfileStat, Long> {

	TellnowProfileStat findByProfile(TellnowProfile profile);

	@Modifying
	@Transactional
	@Query("UPDATE TellnowProfileStat p SET p.queryCount = p.queryCount + :value, p.timeofLastQuery = :date WHERE p.id = :id")
	void incrementQuestionCounter(@Param("id") Long id, @Param("value") Long value, @Param("date") Date date);

	@Modifying
	@Transactional
	@Query("UPDATE TellnowProfileStat p SET p.responseCount =  p.responseCount + :value, p.timeofLastResponse = :date WHERE p.id = :id")
	void incrementAnswerCounter(@Param("id") Long id, @Param("value") Long value, @Param("date") Date date);

	@Modifying
	@Transactional
	@Query("UPDATE TellnowProfileStat p SET p.rewardedResponseCount =  p.rewardedResponseCount + :value, p.timeofLastRewardedResponse = :date WHERE p.id = :id")
	void incrementRewardedAnswerCounter(@Param("id") Long id, @Param("value") Long value, @Param("date") Date date);

	@Query("SELECT p.profile.id FROM TellnowProfileStat p ORDER BY :sortBy DESC")
	Set<Long> getProfiles(@Param("sortBy") String sortBy);
	
}
