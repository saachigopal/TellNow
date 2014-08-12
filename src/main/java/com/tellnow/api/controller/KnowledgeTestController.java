package com.tellnow.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.repository.ProfileRepository;
import com.tellnow.api.service.knowledgetest.KnowledgeTestService;
import com.tellnow.api.utils.knowledgetest.KnowledgeTestUtil;
import com.tellnow.api.utils.knowledgetest.testresponse.KnowledgeTestSubmit;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Knowledge Questions API", description = "Knowledge question related Rest operations")
public class KnowledgeTestController {

	@Autowired
	KnowledgeTestService testService;
	
	@Autowired
	ProfileRepository profileRepository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/api/knowledge/test/{profileId}/create", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> createKnowledgeTest(@PathVariable("profileId") String profileId, 
										@RequestParam(required=false) Integer numberOfQuestions, 
										@RequestParam(required=false) Integer numberOfAnswersPerQuestion) throws TellnowServiceException {
		TellnowResponse resp = new TellnowResponse();
		TellnowProfile profile = profileRepository.findByprofileId(profileId);
		KnowledgeTest test = testService.create(profile, numberOfQuestions, numberOfAnswersPerQuestion);
		KnowledgeTestUtil testUtil = new KnowledgeTestUtil(test);
		resp.setMessage(testUtil);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/knowledge/test/{profileId}/submit", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> submitKnowledgeTest(@PathVariable("profileId") String profileId, 
			@RequestBody KnowledgeTestSubmit testSubmit) throws TellnowServiceException {
		TellnowResponse resp = new TellnowResponse();
		@SuppressWarnings("unused")
		TellnowProfile profile = profileRepository.findByprofileId(profileId);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

}
