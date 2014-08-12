package com.tellnow.api.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.impl.ChatServiceImpl;
import com.tellnow.api.service.impl.TellnowProfileServiceImpl;
import com.tellnow.api.utils.ChatSimple;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Chat API", description = "chat API - rest calls related to chat")
public class ChatController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ChatServiceImpl chatService;

	@Autowired
	TellnowProfileServiceImpl tellnowProfileService;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	private MessageSource messages;

	@Value("${chat.max.messages.between.two}")
	private Long chatMaxMessagesBetweenTwo;

	@RequestMapping(value = "/api/chat", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get all chat", notes = "return chat of one user")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getMessages() {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		List<Chat> messages = (List<Chat>) chatService.getAll();
		resp.setMessage(messages);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/chat/profile/{profileId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get chat of one user", notes = "return chat of one user")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getMessagesOfOneUser(@PathVariable("profileId") String profileId, @RequestParam(required=false, value="type") String type) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		List<Chat> messages = null;
		if(type!=null){
			ChatMessageType messageType = ChatMessageType.fromString(type);
			switch(messageType){
				case RECEIVED:
					messages = (List<Chat>) chatService.getSentTo(profileId);
					break;
				case SENT:
					messages = (List<Chat>) chatService.getSentBy(profileId);
					break;
				default:
					messages = (List<Chat>) chatService.getAll();
			}
		} else {
			messages = (List<Chat>) chatService.getAllBy(profileId);
		}
		resp.setMessage(messages);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/chat/profile/{profileIdSender}/profile/{profileIdSendee}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get chat between two users", notes = "return chat between two users")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getMessagesBetweenTwoUsers(@PathVariable("profileIdSender") String profileIdSender, @PathVariable("profileIdSendee") String profileIdSendee) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		List<Chat> messages = (List<Chat>) chatService.getSentBetween(profileIdSender, profileIdSendee);
		resp.setMessage(messages);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/chat/profile/{profileId}/message/{messageId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get chat of one user", notes = "return chat of one user")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getMessage(@PathVariable("profileId") String profileId, @PathVariable("messageId") String messageId) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		Chat message = null;
		if(messageId!=null){
			message = chatService.getChat(messageId);
		} 
		resp.setMessage(message);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/chat", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "post a message", notes = "post a message")
	public @ResponseBody
	ResponseEntity<TellnowResponse> messagePost(@RequestBody ChatSimple chatSimple) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
//		if (!authService.getLoggedInUser().getProfileId().contentEquals(profileId)) {
//			TellnowError error = new TellnowError(ErrorCodes.unauthorized_operation.getErrorCode(), messages.getMessage(ErrorCodes.unauthorized_operation.getErrorMessageCode(), null, Locale.getDefault()));
//			resp.setError(error);
//			return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
//		}
		TellnowProfile profileSender = tellnowProfileService.getProfile(chatSimple.getSenderId());
		TellnowProfile profileSendee = tellnowProfileService.getProfile(chatSimple.getSendeeId());
		if(profileSender!=null && profileSendee!=null){
			Chat chat = new Chat(profileSender, profileSendee, chatSimple.isAnonymousSender(), chatSimple.isAnonymousSender(), 
									chatSimple.getMessage(), chatSimple.getLocation(), chatSimple.getFiles());
			Long messagesBetween = chatService.getNumberSentBetween(chatSimple.getSenderId(), chatSimple.getSendeeId());
			if(messagesBetween>=chatMaxMessagesBetweenTwo){
				int errCode = GlobalErrorCodes.exceeded_max_number_of_messages.getErrorCode();
				String errMessage = messages.getMessage(GlobalErrorCodes.exceeded_max_number_of_messages.getErrorMessageCode(), null, Locale.getDefault());
				TellnowError err = new TellnowError(errCode, errMessage);
				resp.setError(err);
				response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
			} else {
				chatService.save(chat);
				resp.setMessage(chat);
				response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
			}
		}
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
	
	private enum ChatMessageType {
		SENT("SENT"), RECEIVED("RECEIVED");
		private String name;

		ChatMessageType(String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		public static ChatMessageType fromString(String name) {
			if (name != null) {
				for (ChatMessageType b : ChatMessageType.values()) {
					if (name.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return null;
		}
	}
}