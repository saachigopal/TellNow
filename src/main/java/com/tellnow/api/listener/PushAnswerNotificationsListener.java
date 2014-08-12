package com.tellnow.api.listener;

import javax.persistence.PrePersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tellnow.api.domain.Answer;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.utils.AutowireHelper;

public class PushAnswerNotificationsListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	PushNotificationsService pushNotificationsService;
		
	@PrePersist
	public void sendNotificaton(Answer answer) throws InvalidDeviceException{
		AutowireHelper.autowire(this, this.pushNotificationsService);
		//pushNotificationsService.notifyAnswer(answer);
	}
}
