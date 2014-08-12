package com.tellnow.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Device;
import com.tellnow.api.domain.Location;
import com.tellnow.api.domain.Phone;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.TellnowProfileStat;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.repository.ProfileRepository;
import com.tellnow.api.repository.ProfileStatsRepository;
import com.tellnow.api.repository.RewardPointsRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.DeviceService;
import com.tellnow.api.service.PhoneService;
import com.tellnow.api.service.TellnowProfileService;
import com.tellnow.api.utils.TellNowUtils;

@Service
public class TellnowProfileServiceImpl implements TellnowProfileService {

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	ProfileStatsRepository profileStatsRepository;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	PhoneService phoneService;

	@Autowired
	DeviceService deviceService;

	@Autowired
	private RewardPointsRepository rewardPointsRepository;

	@Override
	public TellnowProfile getProfile(Long id) {
		return profileRepository.findOne(id);
	}

	@Override
	public TellnowProfile getProfile(String profileId) {

		return profileRepository.findByprofileId(profileId);
	}

	@Override
	public TellnowProfile getProfileByUid(String uid) {

		// get MD5 sum from user identifier
		String profileId = TellNowUtils.getProfileId(uid);

		return getProfile(profileId);
	}

	@Override
	public TellnowProfile update(TellnowProfile tProfile, boolean create) {
		if (tProfile != null) {
			if (create == false) {
				TellnowProfile profile = profileRepository.findById(tProfile.getId());
				if (profile != null) {
					if (!profile.equals(tProfile) && profile.getDevices() != null) {
						Device[] devicesDB = profile.getDevices().toArray(new Device[0]);
						for (Device deviceDB : devicesDB) {
							deviceDB.setProfile(null);
							deviceService.save(deviceDB);
						}
					}
					if (!profile.equals(tProfile) && profile.getPhones() != null) {
						Phone[] phonesDB = profile.getPhones().toArray(new Phone[0]);
						for (Phone phoneDB : phonesDB) {
							phoneDB.setProfile(null);
							phoneService.save(phoneDB);
						}
					}
					if (tProfile.getDevices() != null) {
						Device[] devices = tProfile.getDevices().toArray(new Device[0]);
						for (Device device : devices) {
							device.setProfile(profile);
							deviceService.save(device);
						}
					}
					if (tProfile.getPhones() != null) {
						Phone[] phones = tProfile.getPhones().toArray(new Phone[0]);
						for (Phone phone : phones) {
							phone.setProfile(profile);
							phoneService.save(phone);
						}
					}
					profileRepository.save(profile);
				}
			} else {
				profileRepository.save(tProfile);
				if (tProfile.getDevices() != null) {
					Device[] devices = tProfile.getDevices().toArray(new Device[0]);
					for (Device device : devices) {
						device.setProfile(tProfile);
						deviceService.save(device);
					}
				}
				if (tProfile.getPhones() != null) {
					Phone[] phones = tProfile.getPhones().toArray(new Phone[0]);
					for (Phone phone : phones) {
						phone.setProfile(tProfile);
						phoneService.save(phone);
					}
				}

				TellnowProfileStat stat = new TellnowProfileStat();
				stat.setProfile(tProfile);
				stat.setQueryCount(0l);
				stat.setResponseCount(0l);

				profileStatsRepository.save(stat);
			}
		}

		return tProfile;
	}

	@Override
	public String delete(String profileId) {
		TellnowProfile tProfile = profileRepository.findByprofileId(profileId);
		if (tProfile != null) {
			profileRepository.delete(tProfile.getId());
			return tProfile.getProfileId();
		} else {
			return null;
		}
	}

	@Override
	public String delete(Long id) {
		if (profileRepository.exists(id)) {
			TellnowProfile tProfile = profileRepository.findOne(id);
			if (tProfile.getDevices() != null) {
				Device[] devicesDB = tProfile.getDevices().toArray(new Device[0]);
				for (Device deviceDB : devicesDB) {
					deviceDB.setProfile(null);
					deviceService.save(deviceDB);
				}
			}
			if (tProfile.getPhones() != null) {
				Phone[] phonesDB = tProfile.getPhones().toArray(new Phone[0]);
				for (Phone phoneDB : phonesDB) {
					phoneDB.setProfile(null);
					phoneService.save(phoneDB);
				}
			}
			profileRepository.delete(id);
			return tProfile.getProfileId();
		} else {
			return null;
		}
	}

	@Override
	public String delete(TellnowProfile profile) {
		return delete(profile.getId());
	}

	@Override
	public TellnowProfileStat getProfileStats(Long id) {
		return profileStatsRepository.getOne(id);
	}

	@Override
	public TellnowProfileStat getProfileStatsForUser(TellnowProfile profile) {

		return profileStatsRepository.findByProfile(profile);
	}

	@Override
	public TellnowProfileStat getProfileStatsForUser(Long id) {

		TellnowProfile profile = profileRepository.getOne(id);
		return getProfileStatsForUser(profile);
	}

	@Override
	public TellnowProfileStat getProfileStatsForUser(String profileId) {

		TellnowProfile profile = profileRepository.findByprofileId(profileId);
		if (profile != null) {
			return getProfileStatsForUser(profile);
		}
		return null;
	}

	@Override
	public TellnowProfileStat updateLocation(Location location) {

		return updateLocation(authService.getLoggedInUser(), location);
	}

	@Override
	public TellnowProfileStat updateLocation(TellnowProfile profile, Location location) {

		TellnowProfileStat profileStat = getProfileStatsForUser(profile.getId());
		profileStat.setLastKnownLocation(location);
		profileStat = profileStatsRepository.save(profileStat);
		return profileStat;
	}

	@Override
	public TellnowProfileStat incQuestionCounter(Date date) {

		return incQuestionCounter(authService.getLoggedInUser(), date);
	}

	@Override
	public TellnowProfileStat incQuestionCounter(TellnowProfile profile, Date date) {

		TellnowProfileStat profileStat = getProfileStatsForUser(profile);
		profileStatsRepository.incrementQuestionCounter(profileStat.getId(), 1l, date);
		return profileStat;
	}

	@Override
	public TellnowProfileStat incAnswerCounter(Date date) {

		return incAnswerCounter(authService.getLoggedInUser(), date);
	}

	@Override
	public TellnowProfileStat incAnswerCounter(TellnowProfile profile, Date date) {

		TellnowProfileStat profileStat = getProfileStatsForUser(profile.getId());
		profileStatsRepository.incrementAnswerCounter(profileStat.getId(), 1l, date);
		return profileStat;
	}

	@Override
	public TellnowProfileStat incRewardedAnswerCounter(Date date) {

		return incRewardedAnswerCounter(authService.getLoggedInUser(), date);
	}

	@Override
	public TellnowProfileStat incRewardedAnswerCounter(TellnowProfile profile, Date date) {

		TellnowProfileStat profileStat = getProfileStatsForUser(profile.getId());
		profileStatsRepository.incrementRewardedAnswerCounter(profileStat.getId(), 1l, date);
		return profileStat;
	}

	@Override
	public List<TellnowProfile> getAllProfiles() {
		return profileRepository.findAll();
	}

	@Override
	public Set<Long> getTopProfiles(String sortBy) {
		return profileStatsRepository.getProfiles(sortBy);
	}

	@Override
	public Set<TellnowProfile> getProfilesForTopic(Topic topic, Set<Long> exclude) {
		return profileRepository.findByTopic(topic.getId(), exclude);
	}

	@Override
	public Set<TellnowProfile> getProfiles(Set<Long> ids) {
		if(ids == null  || ids.isEmpty()){
			return null;
		}
		return profileRepository.getProfiles(ids);
	}

	@Override
	public Double getAllRewardPoints(Long id) {
		return rewardPointsRepository.getAllRewardPoints(id);
	}

	@Override
	public Double getAllRewardPoints(String profileId) {
		TellnowProfile profile = profileRepository.findByprofileId(profileId);
		if(profile!=null){
			return rewardPointsRepository.getAllRewardPoints(profile.getId());
		} else {
			return null;
		}
	}

	@Override
	public Double getAllRewardPoints(TellnowProfile profile) {
		
		return rewardPointsRepository.getAllRewardPoints(profile.getId());
	}
	
	public Page<TellnowProfile> getProfiles(int pageNr, int itemsPerPage) {
		PageRequest request = new PageRequest(pageNr, itemsPerPage, Sort.Direction.DESC, "profileId");
		return profileRepository.findAll(request);
	}

}
