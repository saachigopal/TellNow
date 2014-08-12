package com.tellnow.api.domain;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.tellnow.api.listener.PushChatNotificationsListener;

@Entity
@Table(name = "chat")
@EntityListeners(PushChatNotificationsListener.class)
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String messageId;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tellnow_profile_sender_id", referencedColumnName="id", nullable=false)
	private TellnowProfile sender;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tellnow_profile_sendee_id", referencedColumnName="id", nullable=false)
	private TellnowProfile sendee;
	
	private boolean anonymousSender;
	
	private boolean anonymousSendee;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "location", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1, prefix = "location_")
	private Location location;
	
	private String message;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "chat_media", 
		joinColumns = { @JoinColumn(name = "chat_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "media_id", referencedColumnName = "id") })
	private Set<MediaFile> mediaFiles;// picture, sound, video

	private Boolean notified;

	private Date date;
	
	public Chat() {
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, String message) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, boolean anonymousSender, boolean anonymousSendee, String message) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, String message, Set<MediaFile> mediaFiles) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.mediaFiles = mediaFiles;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, boolean anonymousSender, boolean anonymousSendee, String message, Set<MediaFile> mediaFiles) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.mediaFiles = mediaFiles;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, String message, Location location, Set<MediaFile> mediaFiles) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.location = location;
		this.mediaFiles = mediaFiles;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(TellnowProfile sender, TellnowProfile sendee, boolean anonymousSender, boolean anonymousSendee, String message, Location location, Set<MediaFile> mediaFiles) {
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.location = location;
		this.mediaFiles = mediaFiles;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(Long id, TellnowProfile sender, TellnowProfile sendee, String message) {
		this.id = id;
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.notified = false;
		this.date = new Date();
	}

	public Chat(Long id, TellnowProfile sender, TellnowProfile sendee, boolean anonymousSender, boolean anonymousSendee, String message) {
		this.id = id;
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.anonymousSender = anonymousSender;
		this.anonymousSendee = anonymousSendee;
		this.message = message;
		this.notified = false;
		this.date = new Date();
	}

	
	public Chat(Long id, TellnowProfile sender, TellnowProfile sendee, String message, Boolean notified) {
		this.id = id;
		this.messageId = UUID.randomUUID().toString();
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.notified = notified;
		this.date = new Date();
	}

	public Chat(Long id, String messageId, TellnowProfile sender, TellnowProfile sendee, String message, Boolean notified) {
		this.id = id;
		this.messageId = messageId;
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.notified = notified;
		this.date = new Date();
	}

	public Chat(Long id, String messageId, TellnowProfile sender, TellnowProfile sendee, String message, Set<MediaFile> mediaFiles, Boolean notified, Date date) {
		this.id = id;
		this.messageId = messageId;
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.mediaFiles = mediaFiles;
		this.notified = notified;
		this.date = date;
	}

	public Chat(String messageId, TellnowProfile sender, TellnowProfile sendee, String message, Set<MediaFile> mediaFiles, Boolean notified, Date date) {
		this.messageId = messageId;
		this.sender = sender;
		this.sendee = sendee;
		this.message = message;
		this.mediaFiles = mediaFiles;
		this.notified = notified;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public TellnowProfile getSender() {
		return sender;
	}

	public void setSender(TellnowProfile sender) {
		this.sender = sender;
	}

	public TellnowProfile getSendee() {
		return sendee;
	}

	public void setSendee(TellnowProfile sendee) {
		this.sendee = sendee;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(Set<MediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public Boolean getNotified() {
		return notified;
	}

	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
