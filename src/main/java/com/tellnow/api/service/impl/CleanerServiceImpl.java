package com.tellnow.api.service.impl;

import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.tellnow.api.service.ChatService;
import com.tellnow.api.service.CleanerService;
import com.tellnow.api.service.MediaService;
import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.service.QuestionService;

@Service
public class CleanerServiceImpl implements CleanerService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final boolean DEFAULT_ACTIVE_VALUE = true;
	private static final int DEFAULT_OLDER_THAN_VALUE = 15;
	private static final String DEFAULT_CRON_EXPRESSION_VALUE = "0 0 1 * * ?";

	private static final String NAME_ACTIVE_ENV = "cleaner.active";
	private static final String NAME_OLDER_THAN_ENV = "cleaner.older.than";
	private static final String NAME_CRON_EXPRESSION_ENV = "cleaner.cron.expression";

	@Autowired
	Environment env;
	
	@Autowired
	QuestionService questionService;

	@Autowired
	ChatService chatService;

	@Autowired
	PushNotificationsService pushNotificationsService;

	@Autowired
	MediaService mediaService;
	
	private boolean active = DEFAULT_ACTIVE_VALUE;
	
	private int olderThan = DEFAULT_OLDER_THAN_VALUE;
	
	private String cronExpression = DEFAULT_CRON_EXPRESSION_VALUE;
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOlderThan() {
		return olderThan;
	}

	public void setOlderThan(int olderThan) {
		this.olderThan = olderThan;
	}

	public String getCronExpression() {
		return cronExpression;
	}


	@Override
	public synchronized void clean() {
	}
	
	@Override
	public synchronized void clean(String cronExpression) {

		this.cronExpression = cronExpression;
		
		String activeStringValue = env.getProperty(NAME_ACTIVE_ENV);
		String olderThanStringValue = env.getProperty(NAME_OLDER_THAN_ENV);
		
		if (isBoolean(activeStringValue)) {
			if (activeStringValue.equals("true")) {
				this.active = true;
			} else {
				this.active = false;
			}
		} else {
			this.active = DEFAULT_ACTIVE_VALUE;
		}
		
		if (isIntegerValue(olderThanStringValue)) {
			this.olderThan = new Integer(olderThanStringValue).intValue();
		} else {
			this.olderThan = DEFAULT_OLDER_THAN_VALUE;
		}
		
		if (active || cronExpression.equalsIgnoreCase("manual")) {
			logger.info("");
			logger.info("===================================================================================================");
			logger.info("========== CLEANER SERVICE STARTED AT " + new DateTime() + " ==========");
			logger.info("==========    Whith parameters: ");
			logger.info("==========       * " + NAME_ACTIVE_ENV + ": " + active);
			logger.info("==========       * " + NAME_OLDER_THAN_ENV + ": " + olderThan);
			logger.info("==========       * " + NAME_CRON_EXPRESSION_ENV + ": " + cronExpression);
			DateTime jodaDate = new DateTime();
			Date javaDate = jodaDate.minusDays(olderThan).toDate();
	
			int notificationsDeleted = pushNotificationsService.deleteNotificationsOlderThan(javaDate);
			logger.info("========== CLEANER SERVICE DELETED " + notificationsDeleted + " NOTIFICATIONS ==========");
			
			int questionsDeleted = questionService.deleteQuestionsOlderThan(javaDate);
			logger.info("========== CLEANER SERVICE DELETED " + questionsDeleted + " QUESTIONS AND THEIR ASSOCIATED ANSWERS ==========");

			int chatMessagesDeleted = chatService.deleteChatMessagesOlderThan(javaDate);
			logger.info("========== CLEANER SERVICE DELETED " + chatMessagesDeleted + " CHAT MESSAGES ==========");

			int mediaFilesDeleted = mediaService.deleteOrphaned();
			logger.info("========== CLEANER SERVICE DELETED " + mediaFilesDeleted + " ORPHANED MEDIA FILES ==========");

			logger.info("========== CLEANER SERVICE ENDED AT " + new DateTime() + " ==========");

			logger.info("===================================================================================================");
			logger.info("");
		} else {
			logger.info("");
			logger.info("===================================================================================================");
			logger.info("========== CLEANER SERVICE NOT STARTED AS SCHEDULED " +
				"(" + cronExpression + ") " +
				"AT " + new DateTime() + " " +
				"BECAUSE IS NOT ACTIVATED IN <<application.properties>> FILE ==========");
			logger.info("==========    SERVICE PARAMETERS ARE: ");
			logger.info("==========       * " + NAME_ACTIVE_ENV + ": " + active);
			logger.info("==========       * " + NAME_OLDER_THAN_ENV + ": " + olderThan);
			logger.info("==========       * " + NAME_CRON_EXPRESSION_ENV + ": " + cronExpression);
			logger.info("===================================================================================================");
			logger.info("");
		}
	}
	
	private boolean isIntegerValue(String s) {
		try {
			@SuppressWarnings("unused")
			Integer i = new Integer(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isBoolean(String s) {
		if (s != null && (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))) {
			return true;
		} else {
			return false;
		}
	}
}
