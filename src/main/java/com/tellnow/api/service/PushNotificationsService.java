package com.tellnow.api.service;

import java.util.Date;
import java.util.List;

import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.InvalidDeviceException;

public interface PushNotificationsService {
	
	public void notifyQuestion(List<TellnowProfile> profiles, Question question) throws InvalidDeviceException, MalformedTokenStringException; 

	public void notifyAnswer(Answer answer) throws InvalidDeviceException, MalformedTokenStringException ;
	
	public void notifyRewardPoints(AnswerReward reward) throws InvalidDeviceException, MalformedTokenStringException ;
	
	public void notifyChat(Chat chat) throws InvalidDeviceException, MalformedTokenStringException;
	
	public int deleteNotificationsOlderThan(Date date);
}
