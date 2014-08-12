package com.tellnow.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.ReportedEntity;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.repository.ReportedEntityRepository;
import com.tellnow.api.service.ReportingService;

@Service
public class ReportingServiceImpl implements ReportingService {

	@Autowired
	ReportedEntityRepository reportedEntityRepository;

	@Autowired
	AnswerServiceImpl answerService;

	@Autowired
	QuestionServiceImpl questionService;

	@Override
	public ReportedEntity reportEntity(ReportedEntity entity) {
		TellnowProfile owner = null;
		switch (entity.getEntityType()) {
			case answer:
				owner = answerService.getOwner(entity.getReportedEntity());
				break;
			case question:
				owner = questionService.getOwner(entity.getReportedEntity());
				break;
		}
		entity.setReportedUserId(owner.getId());
		return reportedEntityRepository.save(entity);
	}

	@Override
	public int sendNotification(ReportedEntity entity) {
		// TODO implement the hole logic related to reported entities
		return 0;
	}

}
