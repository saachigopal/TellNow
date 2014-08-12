package com.tellnow.api.listener;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.tellnow.api.domain.Chat;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.utils.AutowireHelper;

@Configurable
public class PushChatNotificationsListener {

	@Autowired
	PushNotificationsService pushNotificationsService;
	
	@PrePersist
	public void sendNotificaton(Chat chat) throws InvalidDeviceException{
		AutowireHelper.autowire(this, this.pushNotificationsService);
		pushNotificationsService.notifyChat(chat);
	}
}
