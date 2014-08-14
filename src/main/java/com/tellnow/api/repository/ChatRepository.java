package com.tellnow.api.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.TellnowProfile;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	Chat findByMessageId(String messageId);
	
	@Query(value = "SELECT * FROM chat WHERE "
			+ "(tellnow_profile_sender_id=:senderId AND tellnow_profile_sendee_id=:sendeeid) OR "
			+ "(tellnow_profile_sender_id=:sendeeid AND tellnow_profile_sendee_id=:senderId) ORDER BY DATE DESC", nativeQuery = true)
	List<Chat> findSentBetween(@Param("senderId") Long senderId, @Param("sendeeid") Long sendeeid);

	@Query(value = "SELECT * FROM chat WHERE date < :date", nativeQuery = true)
	Set<Chat> findOlderThan(@Param("date") Date date);

	List<Chat> findBySenderOrderByDateDesc(TellnowProfile sender);
	List<Chat> findBySendeeOrderByDateDesc(TellnowProfile sendee);
	List<Chat> findBySenderOrSendeeOrderByDateDesc(TellnowProfile sender, TellnowProfile sendee);
	List<Chat> findBySenderAndSendeeOrderByDateDesc(TellnowProfile sender, TellnowProfile sendee);
	List<Chat> findBySenderAndSendeeAndNotifiedFalseOrderByDateDesc(TellnowProfile sender, TellnowProfile sendee, Boolean notified);
	List<Chat> findAll(Sort sort);
	
	@Modifying(clearAutomatically=true)
	@Transactional
	@Query(value = "DELETE FROM chat WHERE date < :date", nativeQuery = true)
	int deleteChatMessagesOlderThan(@Param("date") Date date);
}
