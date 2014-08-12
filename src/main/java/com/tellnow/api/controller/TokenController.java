package com.tellnow.api.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowProvider;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.VerificationToken;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.repository.VerificationTokenRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.SecurityCodeValidator;
import com.tellnow.api.service.impl.TellnowProfileServiceImpl;
import com.tellnow.api.utils.TellNowUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Token API", description = "Token API - rest calls related to token")
public class TokenController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TellnowProfileServiceImpl profileService;

	@Autowired
	VerificationTokenRepository tokenRepository;

	@Autowired
	SecurityCodeValidator securityCodeValidator;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	private MessageSource messages;

	@Value("${twitter.consumerKey}")
	private String twitterConsumerKey;

	@Value("${twitter.consumerSecret}")
	private String twitterConsumerSecret;

	@Value("${token.expiry.time.in.minutes}")
	private int expiryInMinute;
	
	@RequestMapping(value = "/api/token/{provider}", method = RequestMethod.POST)
	@ApiOperation(value = "Create token", notes = "Creates an access token based on provider and create user profile is not exists", response = VerificationToken.class)
	public @ResponseBody
	ResponseEntity<TellnowResponse> token(@PathVariable("provider") String provider, @RequestParam(value = "uid", required = true) String useridentifier, @RequestParam(value = "accesstoken", required = true) String accesstoken) {

		TellnowProvider tProvider = TellnowProvider.fromString(provider);
		boolean isAuthorized = false;
		List<String> intrestList = null;
		switch (tProvider) {
			case facebook:
				try {
					Facebook facebook = new FacebookTemplate(accesstoken);
					FacebookProfile fprofile = facebook.userOperations().getUserProfile(useridentifier);
					intrestList = fprofile.getInterestedIn();
					isAuthorized = fprofile != null ? true : false;
				} catch (Exception e) {
					logger.error(e.getMessage());//logger.error(e.getMessage(), e);
					isAuthorized = false;
				}
				break;
			case twitter:
				try {
					Twitter twitter = new TwitterTemplate(twitterConsumerKey, twitterConsumerSecret, useridentifier, accesstoken);
					TwitterProfile tProfile = twitter.userOperations().getUserProfile();
					isAuthorized = tProfile != null ? true : false;
				} catch (Exception e) {
					logger.error(e.getMessage());//logger.error(e.getMessage(), e);
					isAuthorized = false;
				}
				break;
			case securitycode:
				isAuthorized = securityCodeValidator.validate(useridentifier, accesstoken);
				break;
		}

		VerificationToken token = null;
		if (isAuthorized) {
			// get MD5 sum from user identifier
			String profileID = TellNowUtils.getProfileId(useridentifier);

			// check
			TellnowProfile profile = profileService.getProfile(profileID);
			if (profile == null) {
				profile = new TellnowProfile();
				profile.setProfileId(profileID);
				profile.setEnableNotifications(true);

				if (intrestList != null && !intrestList.isEmpty()) {
					profile.addInterests(intrestList.toArray(new String[0]));
				}
				profileService.update(profile, true);

				// @Todo get additional information related to user from social provider
			}

			token = tokenRepository.findByProfileID(profileID);
			if (token == null) {// if we didn't find any
				token = new VerificationToken(expiryInMinute);
				token.setProfileID(profileID);
				tokenRepository.save(token);
			} else {
				if (token.hasExpired()) {
					// Probably we will generate another token
					// every time the user will hit us
					token.updateExpiryDate(expiryInMinute);
					tokenRepository.save(token);
				} else {
					token.updateExpiryDate(expiryInMinute);
					tokenRepository.save(token);
				}
			}
		}

		ResponseEntity<TellnowResponse> response = null;
		if (token == null) {
			if (tProvider == TellnowProvider.securitycode) {
				if (!securityCodeValidator.phoneNumberExists()) {
					int errorCode = GlobalErrorCodes.missing_phone_number.getErrorCode();
					String errorMessage = messages.getMessage(GlobalErrorCodes.missing_phone_number.getErrorMessageCode(), null, Locale.getDefault());
					TellnowError error = new TellnowError(errorCode, errorMessage);
					TellnowResponse respBody = new TellnowResponse(error);
					response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.NOT_FOUND);
					return response;
				} else {
					if (securityCodeValidator.securityCodeExpired()) {
						int errorCode = GlobalErrorCodes.expired_code.getErrorCode();
						String errorMessage = messages.getMessage(GlobalErrorCodes.expired_code.getErrorMessageCode(), null, Locale.getDefault());
						TellnowError error = new TellnowError(errorCode, errorMessage);
						TellnowResponse respBody = new TellnowResponse(error);
						response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.NOT_FOUND);
						return response;
					}
				}
				int errorCode = GlobalErrorCodes.unauthorized.getErrorCode();
				String errorMessage = messages.getMessage(GlobalErrorCodes.unauthorized.getErrorMessageCode(), null, Locale.getDefault());
				TellnowError error = new TellnowError(errorCode, errorMessage);
				TellnowResponse respBody = new TellnowResponse(error);
				response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.NOT_FOUND);
			}
		} else {
			TellnowResponse respBody = new TellnowResponse();
			respBody.setMessage(token);
			response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	@ApiOperation(value = "login")
	public @ResponseBody
	void login(@RequestParam(value = "accesstoken", required = true) String accesstoken) {
		VerificationToken vtoken = tokenRepository.findByToken(accesstoken);
		if (vtoken != null) {
			boolean isAuth = authService.login(vtoken);
			if (!isAuth) {
				throw new NotAuthorizedException("Invalid token");
			}
		} else {
			throw new NotAuthorizedException("Invalid token");
		}
	}

	@RequestMapping(value = "/api/logout", method = RequestMethod.GET)
	@ApiOperation(value = "logout")
	public @ResponseBody
	void logout(HttpSession session) {
		
		TellnowProfile profile = authService.getLoggedInUser();
		if(profile!=null){
			profile.clearDevices();
			profileService.update(profile, false);
		}
		session.invalidate();
		authService.logout();
	}
	
	@ExceptionHandler(NotAuthorizedException.class)
	public ResponseEntity<TellnowResponse> handleAllException(Exception ex) {
		logger.error(ex.getMessage(), ex);
		TellnowResponse error = new TellnowResponse();
		error.setError(new TellnowError(GlobalErrorCodes.unauthorized.getErrorCode(), messages.getMessage(GlobalErrorCodes.unauthorized.getErrorMessageCode(), null, Locale.getDefault())));
		ResponseEntity<TellnowResponse> resp = new ResponseEntity<TellnowResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		return resp;
	}
}