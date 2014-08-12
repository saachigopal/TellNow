package com.tellnow.api.controller;

import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.service.TellnowProfileService;
import com.tellnow.api.service.knowledgetest.KnowledgeQuestionService;
import com.tellnow.api.utils.knowledgetest.KnowledgeSimpleResponseUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Knowledge Questions API", description = "Knowledge question related Rest operations")
public class KnowledgeResponseController {

	@Autowired
	KnowledgeQuestionService knowledgeQuestionService;

	@Autowired
	TellnowProfileService profileService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long REWARD_POINTS = 3;
	
	public static final String MESSAGE_GOOD = "Response was good. You received 3 Appreciation points.";
	public static final String MESSAGE_WRONG = "Response was incorrect. You did not receive any Appreciation Points.";
	public static final String MESSAGE_NOQUESTION = "Question does not exists.";
	
	@RequestMapping(value = "/api/knowledge/test/profile/{profileId}/submit", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "get questionId", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> validateResponse(@PathVariable("profileId") String profileId, @RequestBody KnowledgeSimpleResponseUtil response) {
		TellnowResponse resp = new TellnowResponse();
		Double rewardPoints = null;
		try {
			TellnowProfile profile = profileService.getProfile(profileId);
			if(profile!=null){
				KnowledgeQuestion kq = knowledgeQuestionService.getKnowledgeQuestionByPublicId(response.getQuestionId());
				long profilePrivateId = profile.getId();
				if(kq!=null){
					long topicId = kq.getTopic().getId();
					Set<KnowledgeAnswer> goodAnswers = kq.getGoodAnswers();
					boolean good = true;
					if(response.getAnswers()!=null){
						if(goodAnswers!=null){
							if(response.getAnswers().size()!=goodAnswers.size()){
								good = false;
							} else {
								for(KnowledgeAnswer answer : goodAnswers){
									if(!response.getAnswers().contains(answer.getPublicId())){
										good = false;
									}
								}
							}
						} else {
							good = false;
						}
					} else {
						if(goodAnswers!=null){
							good = false;
						} 
					}
					if(good){
						knowledgeQuestionService.updateRewardPoints(profilePrivateId, topicId, REWARD_POINTS);
						resp.setMessage(MESSAGE_GOOD);
					} else {
						resp.setMessage(MESSAGE_WRONG);
					}
				} else {
					resp.setMessage(MESSAGE_NOQUESTION);
				}
			}
			rewardPoints = profileService.getAllRewardPoints(profile);
		} catch (TellnowServiceException e) {
			resp.setError(e.getTellnowError());
		}
		resp.setRewardPoints(rewardPoints);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

}
