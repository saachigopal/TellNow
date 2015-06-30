package com.tellnow.api.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLHandshakeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.ExpiredTokenListener;
import com.relayrides.pushy.apns.FailedConnectionListener;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.Device;
import com.tellnow.api.domain.Notification;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.repository.PushNotificationRepository;
import com.tellnow.api.service.AnswerService;
import com.tellnow.api.service.DeviceService;
import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.service.QuestionService;
import com.tellnow.api.service.TellnowProfileService;

@Service
@SuppressWarnings("unused")
public class PushNotificationsServiceImpl extends Thread implements PushNotificationsService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${APNS.certStore}")
	private String apnsCertStore;

	@Value("${APNS.password}")
	private String apnsPassword;

	@Value("${notifications.notification.alowed.length}")
	private int notificationAllowedLength;

	@Value("classpath:/APNSProduction.p12")
	private Resource cert;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	TellnowProfileService profileService;
	
	@Autowired
	private PushNotificationRepository pushNotificationRepository;
	
	private PushManager<SimpleApnsPushNotification> pushManager;

	private static final String TEMPLATE_FEEDBACK_MESSAGE = "Positive Feedback from %s. You earned %.0f Appreciation points.";
	private static final String TEMPLATE_FEEDBACK_MESSAGE_ANONYMOUS = "Positive Feedback received. You earned %.0f Appreciation points.";
	private static final String TEMPLATE_FEEDBACK_NOTIFICATION = "You received feedback.";
	private static final String TEMPLATE_QUESTION_NOTIFICATION = "You received a question.";
	private static final String TEMPLATE_CHAT_NOTIFICATION = "You received a message.";
	private static final String TEMPLATE_ANSWER_NOTIFICATION = "Answer received from %s.";

	private static final String KEY_QUESTION = "qId";
	private static final String KEY_ANSWER = "aId";
	private static final String KEY_CHAT = "cId";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_NOTIFICATION_ID = "id";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATE = "date";
	private static final String KEY_REWARD_POINTS = "points";

	private static final int TYPE_QUESTION = 1;
	private static final int TYPE_ANSWER = 2;
	private static final int TYPE_FEEDBACK = 3;
	private static final int TYPE_CHAT = 4;

	private static final String SOUND = "default";

	private static final String ACTION_KEY = "View";
	
	private static final int DEFAULT_PAYLOAD_SIZE = 256;
	
	private static final int ADDITIONAL_CHARS = 5;
	
	private static final String STATUS_ELIGIBLE = "Eligible";
	private static final String STATUS_ELIGIBLE_WITH_ENABLED_NOTIFICATIONS = "Eligible with enabled notifications";
	private static final String STATUS_ELIGIBLE_WITH_DISABLED_NOTIFICATIONS_AND_OR_WITHOUT_DEVICES = "Eligible with disabled notifications and/or without devices (possibly not logged in)";
	private static final String STATUS_SENT = "Sent";
	private static final String STATUS_FAILED = "Failed";

	private class APNSNotificationListener implements RejectedNotificationListener<SimpleApnsPushNotification> {
		@Override
		public void handleRejectedNotification(
				PushManager<? extends SimpleApnsPushNotification> pushManager,
				SimpleApnsPushNotification notification,
				RejectedNotificationReason rejectionReason) {
			System.out.format("%s was rejected with rejection reason %s\n", notification, rejectionReason);
			sanitazeIds(TokenUtil.tokenBytesToString(notification.getToken()));			
		}
	}

	private class APNSFailedConnectionListener implements FailedConnectionListener<SimpleApnsPushNotification> {
		@Override
		public void handleFailedConnection(
				PushManager<? extends SimpleApnsPushNotification> pushManager,
				Throwable cause) {
			logger.warn("--- Failed APNS connection!");
		}
	}

	private class APNSExpiredTokensListener implements ExpiredTokenListener<SimpleApnsPushNotification> {

		@Override
		public void handleExpiredTokens(
				PushManager<? extends SimpleApnsPushNotification> arg0,
				Collection<ExpiredToken> arg1) {
			logger.warn("--- Expired APNS tokens!");
		}
		
	}
	
	@PostConstruct
	private void constructApnsService() throws FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		
		pushManager = new PushManager<SimpleApnsPushNotification>(
							ApnsEnvironment.getProductionEnvironment(),
							SSLContextUtil.createDefaultSSLContext(cert.getFile().getAbsolutePath(), apnsPassword),
							null, // Optional: custom event loop group
							null, // Optional: custom ExecutorService for calling listeners
							null, // Optional: custom BlockingQueue implementation
							new PushManagerConfiguration(),
							"Tellnow PushManager");
		
		//final PushManagerFactory<SimpleApnsPushNotification> pushManagerFactory = new PushManagerFactory<SimpleApnsPushNotification>(ApnsEnvironment.getProductionEnvironment(), PushManagerFactory.createDefaultSSLContext(cert.getFile().getAbsolutePath(), apnsPassword));

		//pushManager = pushManagerFactory.buildPushManager();
		pushManager.registerRejectedNotificationListener(new APNSNotificationListener());
		pushManager.registerFailedConnectionListener(new APNSFailedConnectionListener());
		pushManager.registerExpiredTokenListener(new APNSExpiredTokensListener());
		pushManager.start();
	}

	public void notifyQuestion(List<TellnowProfile> profiles, Question question) throws InvalidDeviceException, MalformedTokenStringException {
		for (TellnowProfile profile : profiles) {
			Notification notification = new Notification(profile, new Date()); 
			notification.setText(question.getQuestionText());
			notification.setMessage(getQuestionNotificationPayload(question, notification));
			notification.setStatus(STATUS_ELIGIBLE);
			if (profile.isEnableNotifications() && profile.getDevices() != null && !profile.getDevices().isEmpty()) {
				for (Device device : profile.getDevices()) {
					notification.setDeviceToken(device==null?null:device.getDeviceToken());
					notification.setStatus(STATUS_ELIGIBLE_WITH_ENABLED_NOTIFICATIONS);
					pushNotificationRepository.save(notification);
					send(notification);
					break;
				}
			} else {
				notification.setStatus(STATUS_ELIGIBLE_WITH_DISABLED_NOTIFICATIONS_AND_OR_WITHOUT_DEVICES);
				pushNotificationRepository.save(notification);
			}
		}
	}

	public void notifyAnswer(Answer answer) throws InvalidDeviceException, MalformedTokenStringException {
		List<Device> devices = new ArrayList<Device>();

		if (answer != null && answer.getQuestion() != null && answer.getQuestion().getOwner() != null) {
			TellnowProfile profile = answer.getQuestion().getOwner();
			Notification notification = new Notification(profile, new Date());
			notification.setText(answer.getText());
			notification.setMessage(getAnswerNotificationPayload(answer, notification));
			notification.setStatus(STATUS_ELIGIBLE);
			if (profile.isEnableNotifications() && profile.getDevices() != null && !profile.getDevices().isEmpty()) {
				for (Device device : profile.getDevices()) {
					notification.setDeviceToken(device==null?null:device.getDeviceToken());
					notification.setStatus(STATUS_ELIGIBLE_WITH_ENABLED_NOTIFICATIONS);
					pushNotificationRepository.save(notification);
					send(notification);
					break;
				}
			} else {
				notification.setStatus(STATUS_ELIGIBLE_WITH_DISABLED_NOTIFICATIONS_AND_OR_WITHOUT_DEVICES);
				pushNotificationRepository.save(notification);
			}
		}
	}

	public void notifyRewardPoints(AnswerReward reward) throws InvalidDeviceException, MalformedTokenStringException {
		long questionId = reward.getId().getQuestionId();
		double value = reward.getValue();
		Question question = questionService.getQuestion(questionId);
		String completeName = "";
		String qId = "";
		if (question != null) {
			TellnowProfile profile = question.getOwner();
			qId = question.getQuestionPublicId();
			completeName = profile.getFirstname() + " " + profile.getLastname();
			completeName = completeName.trim();
		}
		long answerId = reward.getId().getAnswerId();
		Answer answer = answerService.getAnswer(answerId);
		if (answer != null) {
			TellnowProfile profile = answer.getOwner();
			Double rewardPoints = profileService.getAllRewardPoints(profile);
			
			Notification notification = new Notification(profile, new Date());
			String payload = "";
			if (answer.getQuestion().isAnonymous()) {
				String s = String.format(TEMPLATE_FEEDBACK_MESSAGE_ANONYMOUS, value);
				notification.setText(s);
				payload = getFeedbackNotificationPayloadAnonymous(qId, value, rewardPoints, notification);
			} else {
				String s = String.format(TEMPLATE_FEEDBACK_MESSAGE, completeName, value);
				notification.setText(s);
				payload = getFeedbackNotificationPayload(qId, completeName, value, rewardPoints, notification);
			}
			notification.setMessage(payload);
			notification.setStatus(STATUS_ELIGIBLE);
			if (profile.isEnableNotifications() && profile.getDevices()!=null && !profile.getDevices().isEmpty()) {
				for (Device device : profile.getDevices()) {
					notification.setDeviceToken(device==null?null:device.getDeviceToken());
					notification.setStatus(STATUS_ELIGIBLE_WITH_ENABLED_NOTIFICATIONS);
					pushNotificationRepository.save(notification);
					send(notification);
					break;
				}
			} else {
				notification.setStatus(STATUS_ELIGIBLE_WITH_DISABLED_NOTIFICATIONS_AND_OR_WITHOUT_DEVICES);
				pushNotificationRepository.save(notification);
			}
		}
	}

	public void notifyChat(Chat chat) throws InvalidDeviceException, MalformedTokenStringException {
		if (chat != null && chat.getSendee() != null) {
			Notification notification = new Notification(chat.getSendee(), new Date());
			notification.setText(chat.getMessage());
			notification.setMessage(getChatNotificationPayload(chat, notification));
			notification.setStatus(STATUS_ELIGIBLE);
			if (chat.getSendee().isEnableNotifications() &&	
				chat.getSendee().getDevices() != null && 
				!chat.getSendee().getDevices().isEmpty()) {
					TellnowProfile prof = chat.getSendee();
					for (Device device : prof.getDevices()) {
						notification.setDeviceToken(device==null?null:device.getDeviceToken());
						notification.setStatus(STATUS_ELIGIBLE_WITH_ENABLED_NOTIFICATIONS);
						pushNotificationRepository.save(notification);
						send(notification);
						break;
				}
			} else {
				notification.setStatus(STATUS_ELIGIBLE_WITH_DISABLED_NOTIFICATIONS_AND_OR_WITHOUT_DEVICES);
				pushNotificationRepository.save(notification);
			}
		}
	}

	private void send(Notification notification) throws MalformedTokenStringException {				
		if (notification!=null && notification.getDeviceToken() != null) {

			final byte[] token = TokenUtil.tokenStringToByteArray("<" + notification.getDeviceToken() + ">");
			try {
				pushManager.getQueue().put(new SimpleApnsPushNotification(token, notification.getMessage()));
				notification.setStatus(STATUS_SENT);
				pushNotificationRepository.save(notification);
			} catch (Exception e) {
				notification.setStatus(STATUS_FAILED);
				pushNotificationRepository.save(notification);
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void removeExpiredTokens(List<Device> devices) {
//		if (devices != null && devices.size() > 0) {
//			try {
//				for (final ExpiredToken expiredToken : pushManager.getExpiredTokens()) {
//					// Stop sending push notifications to each expired token if the expiration
//					// time is after the last time the app registered that token..
//					// System.out.println(">>>>expired token : " + TokenUtil.tokenBytesToString(expiredToken.getToken()));
//					sanitazeIds(devices, TokenUtil.tokenBytesToString(expiredToken.getToken()));
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (FeedbackConnectionException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void sanitazeIds(List<Device> devices, String idToRemove) {
		List<Device> idsToRemove = new ArrayList<Device>();
		for (int i = 0; i < devices.size(); i++) {
			String id = devices.get(i).getDeviceToken().replaceAll("\\s", "");
			if (id.equals(idToRemove)) {
				idsToRemove.add(devices.get(i));
			}
		}
		//System.out.println("---ids   to remove " + idToRemove);
		for (int i = 0; i < idsToRemove.size(); i++) {
			Device d = idsToRemove.get(i);
			devices.remove(d);
			deviceService.remove(d);
		}
	}

	private void sanitazeIds(String idToRemove) {
		deviceService.remove(idToRemove);
	}

	private String getQuestionNotificationPayload(Question question, Notification notification) {
		if (notification!=null) {
			double d = notification.getDate().getTime();
	
			final ApnsPayloadBuilder payloadBuilderwithEmptyMessage = new ApnsPayloadBuilder();
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			payloadBuilder.addCustomProperty(KEY_NOTIFICATION_ID, notification.getNotificationId());
			payloadBuilder.setAlertBody(TEMPLATE_QUESTION_NOTIFICATION);
			payloadBuilder.setBadgeNumber(1);
			payloadBuilder.setSoundFileName(SOUND);
			payloadBuilder.addCustomProperty(KEY_TYPE, TYPE_QUESTION);
			payloadBuilder.addCustomProperty(KEY_DATE, d);
			payloadBuilder.addCustomProperty(KEY_QUESTION, question.getQuestionPublicId());

			return payloadBuilder.buildWithDefaultMaximumLength();
		} else {
			return null;
		}
	}

	private String getAnswerNotificationPayload(Answer answer, Notification notification) {
		if (notification!=null) {
			double d = notification.getDate().getTime();
			String responderName = "";
			if (answer.isAnonymous()) {
				responderName = "Anonymous";
			} else {
				responderName = answer.getOwner().getFirstname() + " " + answer.getOwner().getLastname();
			}
			String s = String.format(TEMPLATE_ANSWER_NOTIFICATION, responderName);
			
			final ApnsPayloadBuilder payloadBuilderwithEmptyMessage = new ApnsPayloadBuilder();
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
	
			payloadBuilder.addCustomProperty(KEY_NOTIFICATION_ID, notification.getNotificationId());
			payloadBuilder.setAlertBody(s);
			payloadBuilder.setBadgeNumber(1);
			payloadBuilder.setSoundFileName(SOUND);
			payloadBuilder.addCustomProperty(KEY_TYPE, TYPE_ANSWER);
			payloadBuilder.addCustomProperty(KEY_DATE, d);
			payloadBuilder.addCustomProperty(KEY_ANSWER, answer.getPublicId());
	
			return payloadBuilder.buildWithDefaultMaximumLength();
		} else {
			return null;
		}
	}

	private String getFeedbackNotificationPayload(String qId, String pointsDonatorName, double pointsValue, double rewardPoints, Notification notification) {
		if (notification!=null) {
			double d = notification.getDate().getTime();
			//String s = String.format(TEMPLATE_FEEDBACK_MESSAGE, pointsDonatorName, pointsValue);
			
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			
			payloadBuilder.addCustomProperty(KEY_NOTIFICATION_ID, notification.getNotificationId());
			payloadBuilder.setAlertBody(TEMPLATE_FEEDBACK_NOTIFICATION);
			payloadBuilder.setBadgeNumber(1);
			payloadBuilder.setSoundFileName(SOUND);
			payloadBuilder.addCustomProperty(KEY_QUESTION, qId);
			payloadBuilder.addCustomProperty(KEY_TYPE, TYPE_FEEDBACK);
			payloadBuilder.addCustomProperty(KEY_DATE, d);
			//payloadBuilder.addCustomProperty(KEY_MESSAGE, s);
			payloadBuilder.addCustomProperty(KEY_REWARD_POINTS, rewardPoints);
			
			return payloadBuilder.buildWithDefaultMaximumLength();
		} else {
			return null;
		}
	}

	private String getFeedbackNotificationPayloadAnonymous(String qId, double pointsValue, double rewardPoints, Notification notification) {
		if (notification!=null) {
			double d = notification.getDate().getTime();
			//String s = String.format(TEMPLATE_FEEDBACK_MESSAGE_ANONYMOUS, pointsValue);
			
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			
			payloadBuilder.addCustomProperty(KEY_NOTIFICATION_ID, notification.getNotificationId());
			payloadBuilder.setAlertBody(TEMPLATE_FEEDBACK_NOTIFICATION);
			payloadBuilder.setBadgeNumber(1);
			payloadBuilder.setSoundFileName(SOUND);
			payloadBuilder.addCustomProperty(KEY_QUESTION, qId);
			payloadBuilder.addCustomProperty(KEY_TYPE, TYPE_FEEDBACK);
			payloadBuilder.addCustomProperty(KEY_DATE, d);
			//payloadBuilder.addCustomProperty(KEY_MESSAGE, s);
			payloadBuilder.addCustomProperty(KEY_REWARD_POINTS, rewardPoints);
	
			return payloadBuilder.buildWithDefaultMaximumLength();
		} else {
			return null;
		}
	}

	private String getChatNotificationPayload(Chat chat, Notification notification) {
		if (notification!=null) {
			double d = notification.getDate().getTime();
			
			final ApnsPayloadBuilder payloadBuilderwithEmptyMessage = new ApnsPayloadBuilder();
			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			payloadBuilder.addCustomProperty(KEY_NOTIFICATION_ID, notification.getNotificationId());
			payloadBuilder.setAlertBody(TEMPLATE_CHAT_NOTIFICATION);
			payloadBuilder.setBadgeNumber(1);
			//payloadBuilder.setSoundFileName(SOUND);
			payloadBuilder.addCustomProperty(KEY_TYPE, TYPE_CHAT);
			payloadBuilder.addCustomProperty(KEY_DATE, d);
			payloadBuilder.addCustomProperty(KEY_CHAT, chat.getMessageId());
			
			return payloadBuilder.buildWithDefaultMaximumLength();
		} else {
			return null;
		}
	}

	@Override
	public int deleteNotificationsOlderThan(Date date) {
		int numberOfDeletions = 0;
		try {
			numberOfDeletions = pushNotificationRepository.deleteNotificationsOlderThan(date);
		} catch (Exception e) {
			logger.error(e.getMessage());// logger.error(e.getMessage(), e);
		}
		return numberOfDeletions;
	}
}
