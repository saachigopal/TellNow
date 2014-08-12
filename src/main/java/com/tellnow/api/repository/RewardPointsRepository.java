package com.tellnow.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.RewardPoints;
import com.tellnow.api.domain.RewardPointsKey;

public interface RewardPointsRepository extends JpaRepository<RewardPoints, RewardPointsKey> {

	@Query("SELECT ar.points FROM RewardPoints ar WHERE ar.id.profileId = :profileId AND ar.id.topicId = :topicId")
	Double getAllRewardPoints(@Param("profileId") Long profileId, @Param("topicId") Long topicId);

	@Query("SELECT sum(ar.points) FROM RewardPoints ar WHERE ar.id.profileId = :profileId")
	Double getAllRewardPoints(@Param("profileId") Long profileId);
	
	//SELECT  * FROM reward_points WHERE profile_id IN (SELECT  profile_id FROM reward_points WHERE profile_id =1 GROUP BY profile_id HAVING MAX(points) = points );
	@Query("SELECT ar FROM RewardPoints ar WHERE ar.id.profileId = :profileId and ar.points = (SELECT MAX(ar.points) FROM RewardPoints ar WHERE ar.id.profileId = :profileId)")
	List<RewardPoints> getTopicWithMaxPoints(@Param("profileId") Long profileId);
	
	@Modifying
	@Transactional
	@Query("UPDATE RewardPoints ar SET ar.points = ( ar.points - :value) WHERE ar.id.profileId = :profileId AND ar.id.topicId = :topicId")
	int decrementRewardPoints(@Param("profileId") Long profileId, @Param("topicId") Long topicId, @Param("value") Double value);

	@Query("SELECT ar.id.profileId FROM RewardPoints ar WHERE ar.id.topicId = :topicId AND ar.id.profileId NOT IN :excludeList ORDER BY ar.points DESC")
	Set<Long> getBestUsersByTopic(@Param("topicId") Long topicId, @Param("excludeList") Set<Long> excludeList);
}
