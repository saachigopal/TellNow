package com.tellnow.api.service;

import com.tellnow.api.domain.ReportedEntity;

public interface ReportingService {

	ReportedEntity reportEntity(ReportedEntity entity);

	int sendNotification(ReportedEntity entity);
}
