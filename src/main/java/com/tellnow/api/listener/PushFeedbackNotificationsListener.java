package com.tellnow.api.listener;

import javax.persistence.PrePersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.utils.AutowireHelper;

@Component
public class PushFeedbackNotificationsListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	PushNotificationsService pushNotificationsService;
		
	@PrePersist
	public void sendNotificaton(AnswerReward answerReward) throws InvalidDeviceException{
		AutowireHelper.autowire(this, this.pushNotificationsService);
		//pushNotificationsService.notifyRewardPoints(answerReward);
	}
}
