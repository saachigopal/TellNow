package com.tellnow.api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tellnow.api.domain.TellnowProfile;

public interface ProfileRepository extends JpaRepository<TellnowProfile, Long> {

	TellnowProfile findByprofileId(String profileId);

	TellnowProfile findById(Long id);

	@Query("SELECT p FROM TellnowProfile p WHERE p.id IN :ids")
	Set<TellnowProfile> getProfiles(@Param("ids") Set<Long> ids);

	@Query("SELECT p FROM TellnowProfile p INNER JOIN p.interests t where t.id = :topicId AND p.id != :loggedUid")
	Set<TellnowProfile> findByTopic(@Param("topicId") Long topicId, @Param("loggedUid") Long loggedUid);

	@Query("SELECT p FROM TellnowProfile p INNER JOIN p.interests t where t.id = :topicId AND p.id NOT IN :exclude")
	Set<TellnowProfile> findByTopic(@Param("topicId") Long topicId, @Param("exclude") Set<Long> exclude);
}
