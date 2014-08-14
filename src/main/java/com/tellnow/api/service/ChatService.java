package com.tellnow.api.service;

import java.util.Collection;
import java.util.Date;

import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.TellnowProfile;

public interface ChatService {

	Chat getChat(Long id);
	
	Chat getChat(String messageId);
	
	Collection<Chat> getAll();
	
	Collection<Chat> getAllBy(String senderOrSendeeProfileId);
	
	Collection<Chat> getAllBy(TellnowProfile senderOrSendee);
	
	Collection<Chat> getSentBy(String senderProfileId);
	
	Collection<Chat> getSentBy(TellnowProfile sender);
	
	Collection<Chat> getSentTo(String sendeeProfileId);
	
	Collection<Chat> getSentTo(TellnowProfile sendee);
	
	Collection<Chat> getSentBetween(String senderProfileId, String sendeeProfileId);
	
	Collection<Chat> getSentBetween(TellnowProfile sender, TellnowProfile sendee);
	
	Long getNumberSentBetween(String senderProfileId, String sendeeProfileId);
	
	Long getNumberSentBetween(TellnowProfile sender, TellnowProfile sendee);
	
	Chat save(Chat chat);
	
	int delete(Chat chat);
	
	int delete(Collection<Chat> chats);
	
	int deleteChatMessagesOlderThan(Date date);
}
