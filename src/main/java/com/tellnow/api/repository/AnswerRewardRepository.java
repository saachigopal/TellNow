package com.tellnow.api.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.domain.AnswerRewardKey;

public interface AnswerRewardRepository extends JpaRepository<AnswerReward, AnswerRewardKey> {

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO answer_reward (`answer_id`, `profile_id`, `question_id`, `topic_id`, `created`, `value`) " + "VALUES (:answerId,:profileId, :questionId, :topicId, :created, :value) ON DUPLICATE KEY UPDATE value = value + :value", nativeQuery = true)
	void updateAnswerRewardPoints(@Param("answerId") Long answerId, @Param("profileId") Long profileId, @Param("questionId") Long questionId, @Param("topicId") Long topicId, @Param("created") Date created, @Param("value") Long value);

	@Query("SELECT ar.id.topicId, SUM(ar.value) as points FROM AnswerReward ar WHERE ar.id.profileId = :profileId GROUP BY ar.id.topicId ORDER BY points DESC")
	Page<Object[]> getUserTopScoredTopics(@Param("profileId") Long profileId, Pageable pageable);

}
