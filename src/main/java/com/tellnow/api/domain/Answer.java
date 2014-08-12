package com.tellnow.api.domain;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DynamicBoost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tellnow.api.listener.PushAnswerNotificationsListener;
import com.tellnow.api.lucene.AnswerBoostStrategy;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "answer")
@Indexed
@DynamicBoost(impl = AnswerBoostStrategy.class)
@EntityListeners(value = {PushAnswerNotificationsListener.class })
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(unique = true)
	private String publicId;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(length = 512)
	private String text;

	private boolean isAnonymous;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@DateBridge(resolution = Resolution.MILLISECOND)
	private Date creationDate;

	@JsonBackReference
	@ManyToOne(targetEntity = Question.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 2)
	private Question question;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "answer_media", joinColumns = { @JoinColumn(name = "answer_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "media_id", referencedColumnName = "id") })
	private Set<MediaFile> mediaFiles;// picture, sound, video

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1)
	private TellnowProfile owner;

	private Integer status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public TellnowProfile getOwner() {
		return owner;
	}

	public void setOwner(TellnowProfile owner) {
		this.owner = owner;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	@JsonGetter
	public String getquestionPublicId(){
		return question!=null?question.getQuestionPublicId():null;
	}


	public Set<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(Set<MediaFile> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public Status getStatus() {
		return this.status != null ? Status.parse(this.status) : Status.getDefault();
	}

	public void setStatus(Status status) {
		this.status = status.getValue();
	}

	public enum Status {
		banned(-1), accepted(1)/* no reward points */, rewarded(2)/* got reward points */, rejected(3)/* bad answer */, pendinng(4), not_set(5);

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