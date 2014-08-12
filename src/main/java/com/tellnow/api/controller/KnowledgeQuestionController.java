package com.tellnow.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.service.knowledgetest.KnowledgeQuestionService;
import com.tellnow.api.utils.knowledgetest.KnowledgeQuestionUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Knowledge Questions API", description = "Knowledge question related Rest operations")
public class KnowledgeQuestionController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	KnowledgeQuestionService knowledgeQuestionService;
	
	@Autowired
	private MessageSource messages;

	@RequestMapping(value = "/api/knowledge/test/profile/{profileId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestionByProfile(@PathVariable("profileId") String profileId) {
		TellnowResponse resp = new TellnowResponse();
		try {
			KnowledgeQuestion kq = validate(knowledgeQuestionService.getKnowledgeQuestionByRandom());
			KnowledgeQuestionUtil kqu = new KnowledgeQuestionUtil(kq);
			resp.setMessage(kqu);
		} catch (TellnowServiceException e) {
			resp.setError(e.getTellnowError());
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/knowledge/test/profile/{profileId}/topic/{topicId}", method = RequestMethod.GET)
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestionByProfileAndTopic(@PathVariable("profileId") String profileId, @PathVariable("topicId") long topicId) {
		TellnowResponse resp = new TellnowResponse();
		try {
			resp.setMessage(validate(knowledgeQuestionService.getKnowledgeQuestionByRandom(topicId)));
		} catch (TellnowServiceException e) {
			resp.setError(e.getTellnowError());
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/knowledge/question/{questionId}", method = RequestMethod.GET)
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestionByQuestionId(@PathVariable("questionId") String questionPublicId) {
		TellnowResponse resp = new TellnowResponse();
		try {
			KnowledgeQuestion kq = validate(knowledgeQuestionService.getKnowledgeQuestionByPublicId(questionPublicId));
			KnowledgeQuestionUtil kqu = new KnowledgeQuestionUtil(kq);
			resp.setMessage(kqu);
		} catch (TellnowServiceException e) {
			resp.setError(e.getTellnowError());
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
	
	private KnowledgeQuestion validate(KnowledgeQuestion knowledgeQuestion) throws TellnowServiceException {
		if (knowledgeQuestion == null){
			throw new TellnowServiceException(5000, "error.message.knowledge.question.not.found", new NullPointerException());
		}
		return knowledgeQuestion;
	}
}
