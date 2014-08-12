package com.tellnow.api.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.repository.RewardPointsRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.NotificationService;
import com.tellnow.api.service.PushNotificationsService;

@Service
public class NotificationServiceImpl implements NotificationService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AnswerServiceImpl answerService;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	ContentServiceImpl contentService;

	@Autowired
	TellnowProfileServiceImpl profileService;

	@Autowired
	PushNotificationsService pushNotificationsService;

	@Autowired
	RewardPointsRepository rewardPointsRepository;

	@Override
	public Set<Long> nomineeRespondersforQuestion(Question question, Integer usersNr) throws InvalidDeviceException {
		logger.info("nomineeRespondersforQuestion: " + usersNr);
		Topic topic = question.getTopic();
		if (topic.getName() == null) {
			topic = contentService.getTopic(question.getTopic().getId());
		}
		logger.info("nomineeRespondersforQuestion: " + topic);
		Set<TellnowProfile> profiles = new HashSet<TellnowProfile>();
		Set<Long> excludeList = new HashSet<Long>();
		excludeList.add(authService.getId());
		// by topic top users

		if (topic != null) {
			logger.info("nomineeRespondersforQuestion 1rst step by topic");
			Set<Long> byTopics = rewardPointsRepository.getBestUsersByTopic(topic.getId(), excludeList);
			if (byTopics != null && !byTopics.isEmpty()) {
				excludeList.addAll(byTopics);
				Set<TellnowProfile> users = profileService.getProfiles(byTopics);
				if (users != null && !users.isEmpty()) {
					profiles.addAll(users);
				}
			}
		}

		// complete the list need optimization exclude the users already added to list
		if (profiles.size() < usersNr) {
			logger.info("nomineeRespondersforQuestion 2nd step by topic 2");
			Set<TellnowProfile> users = profileService.getProfilesForTopic(topic, excludeList);
			if (users != null && !users.isEmpty()) {
				profiles.addAll(users);
			}
		}

		if (profiles.size() < usersNr) {
			logger.info("nomineeRespondersforQuestion 3rd step by answer & reward points");
			Set<Long> candidatesByAnswer = answerService.getProfiles(question.getQuestionText(), topic.getName());
			Set<Long> candidatesByRewards = profileService.getTopProfiles("rewareded_response_count");
			Set<Long> candidates = new HashSet<Long>();

			candidates.addAll(candidatesByAnswer);
			candidates.addAll(candidatesByRewards);

			boolean removed = candidates.remove(authService.getId());
			logger.info("removed me from candidates(candidatesByAnswer/candidatesByRewards) - " + removed);
			Set<TellnowProfile> users = profileService.getProfiles(candidates);
			if (users != null && !users.isEmpty()) {
				profiles.addAll(users);
			}

		}

		if (profiles.size() < usersNr) {
			logger.info("nomineeRespondersforQuestion 4th step profiles:" + profiles);
			Page<TellnowProfile> missingProfiles = profileService.getProfiles(0, usersNr);
			List<TellnowProfile> profileslist = missingProfiles.getContent();
			for (Iterator<TellnowProfile> iterator = profileslist.iterator(); (iterator.hasNext() && profiles.size() < usersNr);) {
				TellnowProfile tellnowProfile = (TellnowProfile) iterator.next();
				if (tellnowProfile.getId().compareTo(authService.getId()) != 0) {
					profiles.add(tellnowProfile);
				}
			}
		}
		logger.info("nomineeRespondersforQuestion profiles:" + profiles);
		if (profiles.size() > usersNr) {
			List<TellnowProfile> list = new ArrayList<TellnowProfile>(profiles);
			profiles = new LinkedHashSet<TellnowProfile>(list.subList(0, usersNr));
		}
		logger.info("nomineeRespondersforQuestion final profiles:" + profiles);
		notifyResponderCandidates(profiles, question);
		return null;
	}

	@Override
	public boolean notifyResponderCandidates(Set<TellnowProfile> candidates, Question question) throws InvalidDeviceException {
		List<TellnowProfile> tellnowProfiles = new ArrayList<TellnowProfile>();
		for (Iterator<TellnowProfile> iterator = candidates.iterator(); iterator.hasNext();) {
			TellnowProfile tellnowProfile = iterator.next();
			// logger.info(tellnowProfile.toString());
			if (tellnowProfile.getId().compareTo(authService.getId()) != 0) {
				tellnowProfiles.add(tellnowProfile);
			}
		}
		pushNotificationsService.notifyQuestion(tellnowProfiles, question);
		return true;
	}

}

