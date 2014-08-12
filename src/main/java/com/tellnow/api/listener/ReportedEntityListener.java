package com.tellnow.api.listener;

import javax.persistence.PostPersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tellnow.api.domain.ReportedEntity;
import com.tellnow.api.service.impl.ReportingServiceImpl;
import com.tellnow.api.utils.AutowireHelper;

public class ReportedEntityListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ReportingServiceImpl reportingService;
		
	@PostPersist
	public void sendNotificaton(ReportedEntity entity){
		AutowireHelper.autowire(this, this.reportingService);
		reportingService.sendNotification(entity);
	}
}
