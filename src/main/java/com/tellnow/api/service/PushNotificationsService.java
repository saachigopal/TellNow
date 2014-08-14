package com.tellnow.api.service;

import java.util.Date;
import java.util.List;

import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.InvalidDeviceException;

public interface PushNotificationsService {
	
	public void notifyQuestion(List<TellnowProfile> profiles, Question question) throws InvalidDeviceException; 

	public void notifyAnswer(Answer answer) throws InvalidDeviceException ;
	
	public void notifyRewardPoints(AnswerReward reward) throws InvalidDeviceException ;
	
	public void notifyChat(Chat chat) throws InvalidDeviceException;
	
	public int deleteNotificationsOlderThan(Date date);
}
