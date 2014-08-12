package com.tellnow.api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tellnow.api.domain.Topic;

public interface TopicsRepository extends JpaRepository<Topic, Long> {

	@Query("SELECT t.id FROM Topic t")
	Set<Long> getAllTopicIds();
}
