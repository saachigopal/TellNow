package com.tellnow.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.Notification;

public interface PushNotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findAll();
	
	@Query(value = "SELECT * FROM notification WHERE profile_id=:profileId ORDER BY date DESC", nativeQuery = true)
	List<Notification> findByProfileIdDesc(@Param("profileId") Long profileId);
	
	@Query("SELECT n FROM Notification n WHERE n.date <  :date")
	List<Notification> getNotificationsOlderThan(@Param("date") Date date);

	@Modifying(clearAutomatically=true)
	@Transactional
	@Query(value = "DELETE FROM notification WHERE date < :date", nativeQuery = true)
	int deleteNotificationsOlderThan(@Param("date") Date date);
}
