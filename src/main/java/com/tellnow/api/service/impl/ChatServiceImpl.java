package com.tellnow.api.service.impl;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.repository.ChatRepository;
import com.tellnow.api.repository.MediaFileRepository;
import com.tellnow.api.repository.ProfileRepository;
import com.tellnow.api.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	EntityManager em;

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	ChatRepository chatRepository;

	@Autowired
	MediaFileRepository mediaFileRepository;

	@Override
	public Chat save(Chat chat) {
		TellnowProfile sender = profileRepository.findByprofileId(chat.getSender().getProfileId());
		TellnowProfile sendee = profileRepository.findByprofileId(chat.getSendee().getProfileId());
		chat.setSender(sender);
		chat.setSendee(sendee);
		if(chat.getMediaFiles()!=null && chat.getMediaFiles().size()>0){
			for(MediaFile mFile : chat.getMediaFiles()){
				mediaFileRepository.save(mFile);
			}
		}
		chatRepository.save(chat);
		return null;
	}


	@Override
	public Chat getChat(Long id) {
		return chatRepository.findOne(id);
	}

	@Override
	public Chat getChat(String messageId) {
		return chatRepository.findByMessageId(messageId);
	}

	@Override
	public Collection<Chat> getAll() {
		return chatRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
	}

	@Override
	public Collection<Chat> getAllBy(String senderOrSendeeProfileId) {
		TellnowProfile profile = profileRepository.findByprofileId(senderOrSendeeProfileId);
		if(profile!=null){
			return chatRepository.findBySenderOrSendeeOrderByDateDesc(profile, profile);
		} else {
			return null; 
		}
	}

	@Override
	public Collection<Chat> getAllBy(TellnowProfile senderOrSendee) {
		return chatRepository.findBySenderOrSendeeOrderByDateDesc(senderOrSendee, senderOrSendee);
	}

	@Override
	public Collection<Chat> getSentBy(String senderProfileId) {
		TellnowProfile profile = profileRepository.findByprofileId(senderProfileId);
		if(profile!=null){
			return chatRepository.findBySenderOrderByDateDesc(profile);
		} else {
			return null; 
		}
	}

	@Override
	public Collection<Chat> getSentBy(TellnowProfile sender) {
		return chatRepository.findBySenderOrderByDateDesc(sender);
	}

	@Override
	public Collection<Chat> getSentTo(String sendeeProfileId) {
		TellnowProfile profile = profileRepository.findByprofileId(sendeeProfileId);
		if(profile!=null){
			return chatRepository.findBySendeeOrderByDateDesc(profile);
		} else {
			return null; 
		}
	}

	@Override
	public Collection<Chat> getSentTo(TellnowProfile sendee) {
		return chatRepository.findBySendeeOrderByDateDesc(sendee);	}

	@Override
	public Collection<Chat> getSentBetween(String senderProfileId, String sendeeProfileId) {
		TellnowProfile senderProfile = profileRepository.findByprofileId(senderProfileId);
		TellnowProfile sendeeProfile = profileRepository.findByprofileId(sendeeProfileId);
		long senderId = senderProfile.getId();
		long sendeeId = sendeeProfile.getId();
		if((senderProfile!=null) && (sendeeProfile!=null)){
			return chatRepository.findSentBetween(senderId, sendeeId);
		} else {
			return null;
		}
	}

	@Override
	public Collection<Chat> getSentBetween(TellnowProfile sender, TellnowProfile sendee) {
		return chatRepository.findBySenderAndSendeeOrderByDateDesc(sender, sendee);
	}

	@Override
	public Long getNumberSentBetween(String senderProfileId, String sendeeProfileId) {
		Collection<Chat> result = getSentBetween(senderProfileId, sendeeProfileId);
		if(result!=null){
			return new Long(result.size());
		} else {
			return new Long(0);
		}
	}

	@Override
	public Long getNumberSentBetween(TellnowProfile sender, TellnowProfile sendee) {
		Collection<Chat> result = getSentBetween(sender, sendee);
		if(result!=null){
			return new Long(result.size());
		} else {
			return new Long(0);
		}
	}
}
