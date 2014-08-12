package com.tellnow.api.listener;

import javax.persistence.PostPersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tellnow.api.domain.Question;
import com.tellnow.api.repository.QuestionRepository;
import com.tellnow.api.service.impl.AdminServiceImpl;
import com.tellnow.api.service.impl.NotificationServiceImpl;
import com.tellnow.api.utils.AutowireHelper;

@Component
public class PushQuestionNotificationsListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private NotificationServiceImpl notificationService;

	@Autowired
	private QuestionRepository questionRepository;

	@Value("${notifications.question.users}")
	private int usersNr;

	@PostPersist
	public void notify(final Question question) {
		AutowireHelper.autowire(this, this.notificationService);
		logger.info(question.getQuestionText() +"-"+ question.getTopic());
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				try {
					notificationService.nomineeRespondersforQuestion(question, AdminServiceImpl.getUserNumber()!=null?AdminServiceImpl.getUserNumber():usersNr);
				} catch (Exception e) {
					logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				}
				super.afterCommit();
			}
		});
	}
}
