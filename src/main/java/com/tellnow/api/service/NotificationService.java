package com.tellnow.api.service;

import java.util.Set;

import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.InvalidDeviceException;

public interface NotificationService {

	Set<Long> nomineeRespondersforQuestion(Question question, Integer maxNumber) throws InvalidDeviceException;

	boolean notifyResponderCandidates(Set<TellnowProfile> candidates, Question question) throws InvalidDeviceException;
}
