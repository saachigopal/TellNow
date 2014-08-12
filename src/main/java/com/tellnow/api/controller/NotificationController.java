package com.tellnow.api.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.Notification;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.repository.NotificationRepository;
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
	NotificationRepository notificationRepository;
	
	@RequestMapping(value = "/api/notification", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get all notification", notes = "return notification of one user")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getNotificationLogs() {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		List<Notification> logs = notificationRepository.findByProfileIdDesc(authService.getId());
		resp.setMessage(logs);
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
}