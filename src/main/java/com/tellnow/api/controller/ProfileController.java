package com.tellnow.api.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.profile.ErrorCodes;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.impl.TellnowProfileServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Profile API", description = "profile API -rest calls related to profile")
public class ProfileController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TellnowProfileServiceImpl tellnowProfileService;

	@Autowired
	AuthServiceImpl authService;

	@Autowired
	private MessageSource messages;

	@RequestMapping(value = "/api/profile/{profileId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get profile", notes = "return profile with the given identifier if exist")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getProfile(@PathVariable("profileId") String profileId) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();

		TellnowProfile profile = tellnowProfileService.getProfile(profileId);
		if (profile == null) {
			int errorCode = ErrorCodes.missing_profile.getErrorCode();
			String errorMessage = messages.getMessage(ErrorCodes.missing_profile.getErrorMessageCode(), null, Locale.getDefault());
			TellnowError error = new TellnowError(errorCode, errorMessage);
			resp.setError(error);
		} else {
			resp.setMessage(profile);
			resp.setRewardPoints(tellnowProfileService.getAllRewardPoints(profile));
		}
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/profile", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "create profile", notes = "update/create profile")
	public @ResponseBody
	ResponseEntity<TellnowResponse> createOrUpdateProfile(@RequestBody TellnowProfile tProfile) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		// if (!authService.getLoggedInUser().getProfileId().contentEquals(profileId)) {
		// TellnowError error = new TellnowError(ErrorCodes.unauthorized_operation.getErrorCode(), messages.getMessage(ErrorCodes.unauthorized_operation.getErrorMessageCode(), null,
		// Locale.getDefault()));
		// resp.setError(error);
		// return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		// }
		TellnowProfile profile = tellnowProfileService.getProfile(tProfile.getProfileId());
		if (profile == null) {
			tellnowProfileService.update(tProfile, true);
		} else {
			tProfile.setId(profile.getId());
			tellnowProfileService.update(tProfile, false);
		}
		resp.setMessage(profile);
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/profile/{profileId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete profile", notes = "delete the given profile")
	public @ResponseBody
	ResponseEntity<TellnowResponse> deleteProfile(@PathVariable("profileId") String profileId) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		// if (!authService.getLoggedInUser().getProfileId().contentEquals(profileId)) {
		// TellnowError error = new TellnowError(ErrorCodes.unauthorized_operation.getErrorCode(), messages.getMessage(ErrorCodes.unauthorized_operation.getErrorMessageCode(), null,
		// Locale.getDefault()));
		// resp.setError(error);
		// return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		// }
		TellnowProfile profile = tellnowProfileService.getProfile(profileId);
		if (profile == null) {
			TellnowError error = new TellnowError(ErrorCodes.missing_profile.getErrorCode(), messages.getMessage(ErrorCodes.missing_profile.getErrorMessageCode(), null, Locale.getDefault()));
			resp.setError(error);
		} else {
			String tProfile = tellnowProfileService.delete(profile.getId());
			resp.setMessage(tProfile);
		}
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/profile/{profileId}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "update profile", notes = "update profile")
	public @ResponseBody
	ResponseEntity<TellnowResponse> updateProfile(@PathVariable("profileId") String profileId, @RequestBody TellnowProfile tProfile) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		// if (!authService.getLoggedInUser().getProfileId().contentEquals(profileId)) {
		// TellnowError error = new TellnowError(ErrorCodes.unauthorized_operation.getErrorCode(), messages.getMessage(ErrorCodes.unauthorized_operation.getErrorMessageCode(), null,
		// Locale.getDefault()));
		// resp.setError(error);
		// return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		// }
		TellnowProfile profile = tellnowProfileService.getProfile(profileId);
		if (profile == null) {
			TellnowError error = new TellnowError(ErrorCodes.missing_profile.getErrorCode(), messages.getMessage(ErrorCodes.missing_profile.getErrorMessageCode(), null, Locale.getDefault()));
			resp.setError(error);
		} else {
			profile.updateNonNull(tProfile);
			tellnowProfileService.update(profile, false);
			resp.setMessage(profile);
		}
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/profile/{profileId}/last-used-device", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "set the last used device", notes = "set the last used device")
	public @ResponseBody
	ResponseEntity<TellnowResponse> updateLastUsedDevice(@PathVariable("profileId") String profileId, @RequestParam(value="devicetoken", required=true) String lastUsedDevice) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();
		// if (!authService.getLoggedInUser().getProfileId().contentEquals(profileId)) {
		// TellnowError error = new TellnowError(ErrorCodes.unauthorized_operation.getErrorCode(), messages.getMessage(ErrorCodes.unauthorized_operation.getErrorMessageCode(), null,
		// Locale.getDefault()));
		// resp.setError(error);
		// return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		// }
		TellnowProfile profile = tellnowProfileService.getProfile(profileId);
		if (profile == null) {
			TellnowError error = new TellnowError(ErrorCodes.missing_profile.getErrorCode(), messages.getMessage(ErrorCodes.missing_profile.getErrorMessageCode(), null, Locale.getDefault()));
			resp.setError(error);
		} else {
//			profile.setLastUsedDevice(lastUsedDevice);
			tellnowProfileService.update(profile, false);
			resp.setMessage(profile);
		}
		response = new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/api/profile/uid/{uid}", method = RequestMethod.GET)
	@ApiOperation(value = "get profile by uid", notes = "generate MD5 sum by uid and return profile if exists")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getProfileByuUid(@PathVariable("uid") String uid) {
		ResponseEntity<TellnowResponse> response = null;
		TellnowResponse resp = new TellnowResponse();

		TellnowProfile profile = tellnowProfileService.getProfileByUid(uid);
		if (profile == null) {
			TellnowError error = new TellnowError(ErrorCodes.missing_profile.getErrorCode(), messages.getMessage(ErrorCodes.missing_profile.getErrorMessageCode(), null, Locale.getDefault()));
			resp.setError(error);
		} else {
			resp.setMessage(profile);
		}
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