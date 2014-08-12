package com.tellnow.api.utils;

import java.util.HashSet;
import java.util.Set;

import com.tellnow.api.domain.Location;
import com.tellnow.api.domain.MediaFile;

public class ChatSimple {
	
	private String messageId;
	private String senderId;
	private String sendeeId;
	private boolean anonymousSender;	
	private boolean anonymousSendee;
	private String message;
	private Location location;
	private Set<MediaFile> files;

	public ChatSimple() {
	}

	public ChatSimple(String senderId, String sendeeId, String message) {
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.message = message;
	}

	public ChatSimple(String senderId, String sendeeId, boolean anonymousSender, boolean anonymousSendee, String message) {
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, String message) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.message = message;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, boolean anonymousSender, boolean anonymousSendee, String message) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, String message, Set<MediaFile> files) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.message = message;
		this.files = files;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, boolean anonymousSender, boolean anonymousSendee, String message, Set<MediaFile> files) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.files = files;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, String message, Location location, Set<MediaFile> files) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.message = message;
		this.location = location;
		this.files = files;
	}

	public ChatSimple(String messageId, String senderId, String sendeeId, boolean anonymousSender, boolean anonymousSendee, String message, Location location, Set<MediaFile> files) {
		this.messageId = messageId;
		this.senderId = senderId;
		this.sendeeId = sendeeId;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.location = location;
		this.files = files;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSendeeId() {
		return sendeeId;
	}

	public void setSendeeId(String sendeeId) {
		this.sendeeId = sendeeId;
	}

	public boolean isAnonymousSender() {
		return anonymousSender;
	}

	public void setAnonymousSender(boolean anonymousSender) {
		this.anonymousSender = anonymousSender;
	}

	public boolean isAnonymousSendee() {
		return anonymousSendee;
	}

	public void setAnonymousSendee(boolean anonymousSendee) {
		this.anonymousSendee = anonymousSendee;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<MediaFile> getFiles() {
		return files;
	}

	public void setFiles(Set<MediaFile> files) {
		this.files = files;
	}
	
	public void addMediaFile(MediaFile mediaFile){
		if(files==null){
			files = new HashSet<MediaFile>();
		}
		files.add(mediaFile);
	}
}
