package com.tellnow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tellnow.api.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findAll();
	
	@Query(value = "SELECT * FROM notification WHERE profile_id=:profileId ORDER BY date DESC", nativeQuery = true)
	List<Notification> findByProfileIdDesc(@Param("profileId") Long profileId);
}
