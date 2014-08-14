package com.tellnow.api.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.listener.PushQuestionNotificationsListener;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "question")
@Indexed
@EntityListeners(value = {PushQuestionNotificationsListener.class })
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(unique = true)
	private String questionPublicId;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(length = 512)
	private String questionText;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@DateBridge(resolution = Resolution.MILLISECOND)
	private Date creationDate;

	@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
	private boolean isAnonymous;

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "topic_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1)
	private Topic topic;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "owner", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private TellnowProfile owner;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "question_media", joinColumns = { @JoinColumn(name = "question_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "media_id", referencedColumnName = "id") })
	private Collection<MediaFile> mediaFiles;// picture, sound, video

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<Answer> answers;

	private Date expiryDate;
	
	@Column(columnDefinition = "int(11) default '10'")
	private Integer maxAnswersAllowed;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "location", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1, prefix = "location_")
	private Location location;
	
	private Integer status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionPublicId() {
		return questionPublicId;
	}

	public void setQuestionPublicId(String questionPublicId) {
		this.questionPublicId = questionPublicId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public TellnowProfile getOwner() {
		return owner;
	}

	public void setOwner(TellnowProfile owner) {
		this.owner = owner;
	}

	public Collection<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(Collection<MediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getMaxAnswersAllowed() {
		return maxAnswersAllowed;
	}

	public void setMaxAnswersAllowed(Integer maxAnswersAllowed) {
		this.maxAnswersAllowed = maxAnswersAllowed;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Status getStatus() {
		return this.status != null ? Status.parse(this.status) : Status.getDefault();
	}

	public void setStatus(Status status) {
		this.status = status.getValue();
	}

	@Transient
	public boolean hasExpired() {
		DateTime tokenDate = new DateTime(getExpiryDate());
		return tokenDate.isBeforeNow();
	}

	@Transient
	public Set<String> getFiles() {
		Set<String> result = new HashSet<String>();
		if (this.mediaFiles != null) {
			for (Iterator<MediaFile> iterator = mediaFiles.iterator(); iterator.hasNext();) {
				MediaFile file = (MediaFile) iterator.next();
				result.add(file.getPath());
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("Question: %s\n" + " text: %s\n", this.questionPublicId, this.questionText);
	}
	
	public enum Status {
		banned(-1), not_set(1);

		private int value;

		Status(int value) {
			this.value = value;
		}

		public static Status getDefault() {
			return not_set;
		}

		public int getValue() {
			return value;
		}

		public static Status parse(int id) {
			Status dStatus = not_set; // Default
			for (Status item : Status.values()) {
				if (item.getValue() == id) {
					dStatus = item;
					break;
				}
			}
			return dStatus;
		}
	}
}
