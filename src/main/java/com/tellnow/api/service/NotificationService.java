package com.tellnow.api.service;

import java.util.Set;

import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.InvalidDeviceException;

public interface NotificationService {

	Set<Long> nomineeRespondersforQuestion(Question question, Integer maxNumber) throws InvalidDeviceException, MalformedTokenStringException;

	boolean notifyResponderCandidates(Set<TellnowProfile> candidates, Question question) throws InvalidDeviceException, MalformedTokenStringException;
}
