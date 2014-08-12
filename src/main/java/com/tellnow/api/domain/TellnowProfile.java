package com.tellnow.api.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModel;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "tellnow_profile")
@ApiModel(value = "profile")
public class TellnowProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(unique = true)
	private String profileId;

	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "picture", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private MediaFile picture;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String email;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String firstname;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String lastname;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(name = "birth_date")
	private Date birthDate;

	
	@OneToOne(optional = true)
	@JoinColumn(name = "age_range_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private AgeRange ageRange;

	private Integer gender;

	@Column(length = 5)
	private String locale;

	@OneToOne(optional = true)
	@JoinColumn(name = "religion_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Religion religion;

	@Column(name = "political_affiliation")
	private String politicalAffiliation;

	
	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1)
	private Address address;
	
	private String work;
	
	private boolean enableNotifications = true;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "profile")
	@IndexedEmbedded(depth = 1)
	private Set<Phone> phones;

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "education_level_id", referencedColumnName = "id")
	@IndexedEmbedded(depth = 1)
	private EducationLevel education_level;

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "education_sub_discipline_id", referencedColumnName = "id")
	@IndexedEmbedded(depth = 1)
	private EducationSubDiscipline subdiscipline;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "interests", 
		joinColumns = { @JoinColumn(name = "profile_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "topic_id", referencedColumnName = "id") })
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded(depth = 1)
	private Set<Topic> interests;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "profile")
	private Set<Device> devices;

	@JsonIgnore
	@OneToOne(mappedBy = "profile", orphanRemoval = true)
	@IndexedEmbedded(depth = 1)
	private TellnowProfileStat profileStat;

	public TellnowProfile() {
	}

	public TellnowProfile(Long id) {
		this.id = id;
	}

	public TellnowProfile(TellnowProfile prof){
		this.id = prof.getId();
		this.profileId = prof.getProfileId();
		this.picture = prof.getPicture();
		this.email = prof.getEmail();
		this.firstname = prof.getFirstname();
		this.lastname = prof.getLastname();
		this.birthDate = prof.getBirthDate();
		this.ageRange = prof.getAgeRange();
		this.gender = prof.getGender().value;
		this.locale = prof.getLocale();
		this.religion = prof.getReligion();
		this.address = prof.getAddress();
		this.work = prof.getWork();
		this.enableNotifications = prof.isEnableNotifications();
		this.addPhonesNonCumulative(prof.phonesAsStringSet());
		this.education_level = prof.getEducation_level();
		this.subdiscipline = prof.getEducationSubDiscipline();
		this.interests = prof.getInterests();
		this.addDevicesNonCumulative(prof.devicesAsStringSet());
		this.profileStat = prof.getProfileStat();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public MediaFile getPicture() {
		return picture;
	}

	public void setPicture(MediaFile picture) {
		this.picture = picture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public AgeRange getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(AgeRange ageRange) {
		this.ageRange = ageRange;
	}

	public Gender getGender() {
		return this.gender != null ? Gender.parse(this.gender) : Gender.getDefault();
	}

	public void setGender(Gender gender) {
		this.gender = gender.getValue();
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Religion getReligion() {
		return religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
	}

	public String getPoliticalAffiliation() {
		return politicalAffiliation;
	}

	public void setPoliticalAffiliation(String politicalAffiliation) {
		this.politicalAffiliation = politicalAffiliation;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public boolean isEnableNotifications() {
		return enableNotifications;
	}

	public void setEnableNotifications(boolean enableNotifications) {
		this.enableNotifications = enableNotifications;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public Set<Topic> getInterests() {
		return interests;
	}

	public EducationLevel getEducation_level() {
		return education_level;
	}

	public void setEducation_level(EducationLevel education_level) {
		this.education_level = education_level;
	}

	public EducationSubDiscipline getEducationSubDiscipline() {
		return subdiscipline;
	}

	public void setEducationSubDiscipline(EducationSubDiscipline subdiscipline) {
		this.subdiscipline = subdiscipline;
	}

	public void setInterests(Set<Topic> interests) {
		this.interests = interests;
	}

	public void addInterest(Topic topic) {
		if (interests == null) {
			interests = new HashSet<Topic>();
		}
		interests.add(topic);
	}

	public void addInterest(String topic) {
		if (interests == null) {
			interests = new HashSet<Topic>();
		}
		interests.add(new Topic(topic));
	}

	public void addInterests(Topic... topics) {
		if (interests == null) {
			interests = new HashSet<Topic>();
		}
		for (Topic topic : topics) {
			interests.add(topic);
		}
	}

	public void addInterests(String... topics) {
		if (interests == null) {
			interests = new HashSet<Topic>();
		}
		for (String topic : topics) {
			interests.add(new Topic(topic));
		}
	}

	public Phone getPhone(String phoneNumber) {
		if(this.devices==null || !this.devicesAsStringSet().contains(phoneNumber)){
			return null;
		} else {
			for(Phone ph : this.phones){
				if(ph.getPhoneNumber().equals(phoneNumber)){
					return ph;
				} 
			}
		}
		return null;
	}

	public Set<String> phonesAsStringSet() {
		if(phones==null){
			return null;
		} else {
			Set<String> result = new HashSet<String>();
			for(Phone phone : phones){
				result.add(phone.getPhoneNumber());
			}
			return result;
		}
	}

	public void setPhones(Set<Phone> phones) {
		if (phones==null){
			this.clearPhones();
			this.phones = null;
		} else {
			this.addPhonesNonCumulative(phones);
		}
	}

	public void addPhoneCumulative(Phone phone) {
		if (this.phones == null) {
			this.phones = new HashSet<Phone>();
		}
		phone.setProfile(this);
		this.phones.add(phone);
	}

	public void addPhoneNonCumulative(Phone phone) {
		if (this.phones == null) {
			this.phones = new HashSet<Phone>();
			phone.setProfile(this);
			this.phones.add(phone);
		} else {
			this.clearPhones();
			phone.setProfile(this);
			this.phones.add(phone);
		}	
	}

	public void clearPhones() {
		if(this.phones != null){
			Phone[] phoneArr = this.phones.toArray(new Phone[0]);
			for(Phone phone : phoneArr){
				this.removePhone(phone);
				phone.setProfile(null);
			}
		}
	}

	public void addPhoneCumulative(String phone) {
		if(phones != null){
			if(!this.phonesAsStringSet().contains(phone)){
				addPhoneCumulative(new Phone(phone));
			}
		} else {
			addPhoneCumulative(new Phone(phone));
		}
	}

	public void addPhoneNonCumulative(String phone) {
		addPhoneCumulative(new Phone(phone));
	}

	public void addPhonesCumulative(Phone... phoneArr) {
		for (Phone phone : phoneArr){
			addPhoneCumulative(phone);
		}
	}

	public void addPhonesNonCumulative(Phone... phoneArr) {
		this.clearPhones();
		for (Phone phone : phoneArr){
			addPhoneCumulative(phone);
		}
	}

	public void addPhonesCumulative(String... phoneArr) {
		for (String phone : phoneArr){
			addPhoneCumulative(phone);
		}
	}

	public void addPhonesNonCumulative(String... phoneArr) {
		this.clearPhones();
		for (String phone : phoneArr){
			addPhoneCumulative(phone);
		}
	}

	public void addPhonesCumulative(Collection<?> phoneCol) {
		for (Object phone : phoneCol){
			if(phone instanceof Phone){
				addPhoneCumulative((Phone) phone);
			} else {
				if(phone instanceof String){
					addPhoneCumulative((String) phone);
				}
			}
		}
	}

	public void addPhonesNonCumulative(Collection<?> phoneCol) {
		this.clearPhones();
		for (Object phone : phoneCol){
			if(phone instanceof Phone){
				addPhoneCumulative((Phone) phone);
			} else {
				if(phone instanceof String){
					addPhoneCumulative((String) phone);
				}
			}
		}
	}

	public void removePhone(Phone phone) {
		if (this.phones==null || !this.phones.contains(phone)){
			return;
		}
		this.phones.remove(phone);
		phone.setProfile(null);
	}

	public void removePhone(String phone) {
		if (this.phones==null || !this.phonesAsStringSet().contains(phone)){
			return;
		}
		Phone ph = this.getPhone(phone);
		if(ph!=null){
			this.phones.remove(this.getPhone(phone));
			ph.setProfile(null);
		}
	}

	public void removePhones(Phone... phones) {
		if (this.phones==null) {
			return;
		}
		for(Phone phone : phones){
			this.removePhone(phone);
		}
	}

	public void removePhones(String... phones) {
		if (this.phones==null) {
			return;
		}
		for(String phone : phones){
			this.removePhone(phone);
		}
	}

	public void removePhones(Collection<?>... phones) {
		if (this.phones==null) {
			return;
		}
		for(Object phone : phones){
			if(phone instanceof Phone){
				this.removePhone((Phone) phone);
			} else {
				if(phone instanceof String){
					this.removePhone((String) phone);
				}
			}
		}
	}

	public Device getDevice(String device) {
		if(this.devices==null || !this.devicesAsStringSet().contains(device)){
			return null;
		} else {
			for(Device dev : this.devices){
				if(dev.getDeviceToken().equals(device)){
					return dev;
				} 
			}
		}
		return null;
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public Set<String> devicesAsStringSet() {
		if(devices==null){
			return null;
		} else {
			Set<String> result = new HashSet<String>();
			for(Device device : devices){
				result.add(device.getDeviceToken());
			}
			return result;
		}
	}

	public void setDevices(Set<Device> devices) {
		if (devices==null){
			this.clearDevices();
			this.devices = null;
		} else {
			this.addDevicesNonCumulative(devices);
		}
	}

	public void updateLastUsedDevice(Device device){
		if(this.devices!=null && this.devices.contains(device)){
			for(Device dev : devices){
				if(dev.equals(device)){
					dev.setLastUsed(true);
				} else {
					dev.setLastUsed(false);
				}
			}
		}
	}

	public void updateLastUsedDevice(String device){
		if(this.devices!=null && this.devicesAsStringSet().contains(device)){
			for(Device dev : devices){
				if(dev.getDeviceToken().equals(device)){
					dev.setLastUsed(true);
				} else {
					dev.setLastUsed(false);
				}
			}
		}
	}

	public void addDeviceCumulative(Device device) {
		if (this.devices == null) {
			this.devices = new HashSet<Device>();
		}
		device.setProfile(this);
		this.devices.add(device);
	}

	public void addDeviceNonCumulative(Device device) {
		if (this.devices == null) {
			this.devices = new HashSet<Device>();
			device.setProfile(this);
			this.devices.add(device);
		} else {
			this.clearDevices();
			device.setProfile(this);
			this.devices.add(device);
		}		
	}

	public void clearDevices() {
		if(this.devices != null){
			Device[] deviceArr = this.devices.toArray(new Device[0]);
			for(Device device : deviceArr){
				this.removeDevice(device);
				device.setProfile(null);
			}
		}
	}

	public void addDeviceCumulative(String device) {
		if(devices != null){
			if(!this.devicesAsStringSet().contains(device)){
				addDeviceCumulative(new Device(device));
			}
		} else {
			addDeviceCumulative(new Device(device));
		}
	}

	public void addDeviceNonCumulative(String device) {
		addDeviceNonCumulative(new Device(device));
	}

	public void addDevicesCumulative(Device... deviceArr) {
		for (Device device : deviceArr){
			addDeviceCumulative(device);
		}
	}

	public void addDevicesNonCumulative(Device... deviceArr) {
		this.clearDevices();
		for (Device device : deviceArr){
			addDeviceCumulative(device);
		}
	}

	public void addDevicesCumulative(String... deviceArr) {
		for (String device : deviceArr){
			addDeviceCumulative(device);
		}
	}

	public void addDevicesNonCumulative(String... deviceArr) {
		this.clearDevices();
		for (String device : deviceArr){
			addDeviceCumulative(device);
		}
	}

	public void addDevicesCumulative(Collection<?> deviceCol) {
		for (Object device : deviceCol){
			if(device instanceof Device){
				addDeviceCumulative((Device) device);
			} else {
				if(device instanceof String){
					addDeviceCumulative((String) device);
				}
			}
		}
	}

	public void addDevicesNonCumulative(Collection<?> deviceCol) {
		this.clearDevices();
		for (Object device : deviceCol){
			if(device instanceof Device){
				addDeviceCumulative((Device) device);
			} else {
				if(device instanceof String){
					addDeviceCumulative((String) device);
				}
			}
		}
	}

	public void removeDevice(Device device) {
		if (this.devices==null || !this.devices.contains(device)){
			return;
		}
		this.devices.remove(device);
		device.setProfile(null);
	}

	public void removeDevice(String device) {
		if (this.devices==null || !this.devicesAsStringSet().contains(device)){
			return;
		}
		Device dev = this.getDevice(device);
		if(dev!=null){
			this.devices.remove(this.getDevice(device));
			dev.setProfile(null);
		}
	}

	public void removeDevices(Device... devices) {
		if (this.devices==null) {
			return;
		}
		for(Device device : devices){
			this.removeDevice(device);
		}
	}

	public void removeDevices(String... devices) {
		if (this.devices==null) {
			return;
		}
		for(String device : devices){
			this.removeDevice(device);
		}
	}

	public void removeDevices(Collection<?>... devices) {
		if (this.devices==null) {
			return;
		}
		for(Object device : devices){
			if(device instanceof Device){
				this.removeDevice((Device) device);
			} else {
				if(device instanceof String){
					this.removeDevice((String) device);
				}
			}
		}
	}

	public TellnowProfileStat getProfileStat() {
		return profileStat;
	}

	public void setProfileStat(TellnowProfileStat profileStat) {
		this.profileStat = profileStat;
	}

	public void update(TellnowProfile prof){
		this.id = prof.getId();
		this.profileId = prof.getProfileId();
		this.picture = prof.getPicture();
		this.email = prof.getEmail();
		this.firstname = prof.getFirstname();
		this.lastname = prof.getLastname();
		this.birthDate = prof.getBirthDate();
		this.ageRange = prof.getAgeRange();
		this.gender = prof.getGender().value;
		this.locale = prof.getLocale();
		this.religion = prof.getReligion();
		this.address = prof.getAddress();
		this.addPhonesNonCumulative(prof.phonesAsStringSet());
		this.education_level = prof.getEducation_level();
		this.subdiscipline = prof.getEducationSubDiscipline();
		this.work = prof.getWork();
		this.enableNotifications = prof.isEnableNotifications();
		this.interests = prof.getInterests();
		this.addDevicesNonCumulative(prof.devicesAsStringSet());
		this.profileStat = prof.getProfileStat();
	}

	public void updateNonNull(TellnowProfile prof){
		if(prof.getId()!=null){
			this.id = prof.getId();
		}
		if(prof.getProfileId()!=null){
			this.profileId = prof.getProfileId();
		}
		if(prof.getPicture()!=null){
			this.picture = prof.getPicture();
		}
		if(prof.getEmail()!=null){
			this.email = prof.getEmail();
		}
		if(prof.getFirstname()!=null){
			this.firstname = prof.getFirstname();
		}
		if(prof.getLastname()!=null){
			this.lastname = prof.getLastname();
		}
		if(prof.getBirthDate()!=null){
			this.birthDate = prof.getBirthDate();
		}
		if(prof.getAgeRange()!=null){
			this.ageRange = prof.getAgeRange();
		}
		if(prof.getGender()!=null){
			this.gender = prof.getGender().value;
		}
		if(prof.getLocale()!=null){
			this.locale = prof.getLocale();
		}
		if(prof.getReligion()!=null){
			this.religion = prof.getReligion();
		}
		if(prof.getAddress()!=null){
			this.address = prof.getAddress();
		}
		if(prof.phonesAsStringSet()!=null){
			this.addPhonesNonCumulative(prof.phonesAsStringSet());
		}
		if(prof.getEducation_level()!=null){
			this.education_level = prof.getEducation_level();
		}
		if(prof.getEducationSubDiscipline()!=null){
			this.subdiscipline = prof.getEducationSubDiscipline();
		}
		this.enableNotifications = prof.isEnableNotifications();
		if(prof.getWork()!=null){
			this.work = prof.getWork();
		}
		if(prof.getInterests()!=null){
			this.interests = prof.getInterests();
		}
		if(prof.devicesAsStringSet()!=null){
			this.addDevicesNonCumulative(prof.devicesAsStringSet());
		}
		if(prof.getProfileStat()!=null){
			this.profileStat = prof.getProfileStat();
		}
	}

	@Override
	public String toString() {
		return String.format("profileID: %s\n" + " email: %s\n", this.profileId, this.email);
	}

	public enum Gender {
		male(1), female(2), other(3), not_specified(4);

		private int value;

		Gender(int value) {
			this.value = value;
		}

		public static Gender getDefault() {
			return not_specified;
		}

		public int getValue() {
			return value;
		}

		public static Gender parse(int id) {
			Gender gender = not_specified; // Default
			for (Gender item : Gender.values()) {
				if (item.getValue() == id) {
					gender = item;
					break;
				}
			}
			return gender;
		}
	};
}
