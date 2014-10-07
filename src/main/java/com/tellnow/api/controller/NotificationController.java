package com.tellnow.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.Notification;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.repository.PushNotificationRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Chat API", description = "chat API - rest calls related to chat")
public class NotificationController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private AuthServiceImpl authService; 
	
	@Autowired
	PushNotificationRepository notificationRepository;
	
	@RequestMapping(value = "/api/notification", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get all notification", notes = "return notifications of one user")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getNotificationLogs(
			@RequestParam(value = "timestamp", required = false) Long newerThanInMillis,
			@RequestParam(value = "howmany", required = false) Integer howMany) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		List<Notification> logs = new ArrayList<Notification>();
		if (newerThanInMillis == null) {
			logs = notificationRepository.findVisibleOnesByProfileIdDesc(authService.getId());
		} else {
			Date newerThan = new Date(newerThanInMillis);
			logs = notificationRepository.findVisibleOnesByProfileIdNewerThanDesc(authService.getId(), newerThan);
		}
		if (howMany != null) {
			logs = getFirstElementsFromList(logs, howMany);
		}
		resp.setMessage(logs);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/notification/{notificationId}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "delete notification", notes = "delete notification")
	public @ResponseBody
	ResponseEntity<TellnowResponse> deleteNotificationLog(@PathVariable("notificationId") String notificationId) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		int r = notificationRepository.setInvisibleNotification(authService.getId(), notificationId);
		resp.setMessage("Deleted (set invisible) " + r + " notifications");
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<TellnowResponse> handleAllException(Exception ex) {
		logger.error(ex.getMessage(), ex);
		TellnowResponse error = new TellnowResponse();
		error.setError(new TellnowError(GlobalErrorCodes.internal_server_error.getErrorCode(), messages.getMessage(GlobalErrorCodes.internal_server_error.getErrorMessageCode(), null, Locale.getDefault())));
		ResponseEntity<TellnowResponse> resp = new ResponseEntity<TellnowResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		return resp;
	}
	
	private <T> List<T> getFirstElementsFromList(List<T> input, int numberOfElements) {
		List<T> result = new ArrayList<T>();
		if (input==null) {
			return null;
		}
		if (numberOfElements > input.size()) {
			return input;
		}
		if (numberOfElements > 0) {
			for (int i=0; i<numberOfElements; i++) {
				result.add(input.get(i));
			}
		}
		return result;
	}
}