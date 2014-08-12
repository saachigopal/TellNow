package com.tellnow.api.controller;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.answer.ErrorCodes;
import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Answer.Status;
import com.tellnow.api.exceptions.DuplicateAnswerException;
import com.tellnow.api.exceptions.QuestionAnswerLimitException;
import com.tellnow.api.exceptions.QuestionExpiredException;
import com.tellnow.api.exceptions.QuestionMissingException;
import com.tellnow.api.question.SortAnswersBy;
import com.tellnow.api.service.impl.AnswerServiceImpl;
import com.tellnow.api.service.impl.TellnowProfileServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Answers API", description = "API calls related to answers")
public class AnswerController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TellnowProfileServiceImpl profileService;

	@Autowired
	private AnswerServiceImpl answerService;

	@Autowired
	private MessageSource messages;

	@RequestMapping(value = "/api/answer", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "add answer", notes = "returns the newly added answer ")
	public ResponseEntity<TellnowResponse> addAnswer(@RequestBody Answer answer) {

		TellnowResponse resp = new TellnowResponse();
		Answer answerEntity = null;
		try {
			answerEntity = answerService.addAnswer(answer);
		} catch (QuestionMissingException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.missing_question.getErrorCode(), messages.getMessage(ErrorCodes.missing_question.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (QuestionExpiredException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.expired_question.getErrorCode(), messages.getMessage(ErrorCodes.expired_question.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (QuestionAnswerLimitException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.answer_limit.getErrorCode(), messages.getMessage(ErrorCodes.answer_limit.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (DuplicateAnswerException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.duplicate_answer.getErrorCode(), messages.getMessage(ErrorCodes.duplicate_answer.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		if (answerEntity != null) {
			if (answer.getOwner() != null) {
				Double rewardPoints = profileService.getAllRewardPoints(answerEntity.getOwner());
				resp.setRewardPoints(rewardPoints);
			}
			resp.setMessage(answerEntity);
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/answer/{answer}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get answer", notes = "returns answer ")
	public ResponseEntity<TellnowResponse> getAnswer(@PathVariable("answer") String answerPublicId) {

		TellnowResponse resp = new TellnowResponse();
		Answer answerEntity = null;
		try {
			answerEntity = answerService.getAnswer(answerPublicId);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answerEntity);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@PreAuthorize("isAuthenticated() and hasPermission(#question, 'isOwner')")
	@RequestMapping(value = "/api/answers/{question}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get answers for question", notes = "returns all answers ")
	public ResponseEntity<TellnowResponse> getAnswers(@PathVariable("question") String question) {

		TellnowResponse resp = new TellnowResponse();
		List<Answer> answerEntities = null;
		try {
			answerEntities = answerService.getAnswers(question);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answerEntities);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/answer/{answer}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "delete answer", notes = "delete and returns the deleted answer ")
	public ResponseEntity<TellnowResponse> delete(@PathVariable("answer") String answerPublicId) {

		TellnowResponse resp = new TellnowResponse();
		Answer answerEntity = null;
		try {
			answerEntity = answerService.delete(answerPublicId);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answerEntity);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	// replaced by request
	// @RequestMapping(value = "/api/answer/reward/{answer}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	// @ApiOperation(value = "reward answer", notes = "reward answer/response ")
	// public ResponseEntity<TellnowResponse> reward(@PathVariable("answer") String answerPublicId, @RequestParam(value = "points", required = true) Integer rewardpoints) {
	//
	// TellnowResponse resp = new TellnowResponse();
	// boolean answerEntity = false;
	// try {
	// answerEntity = answerService.reward(answerPublicId, rewardpoints, Status.rewarded, true);
	// } catch (Exception e) {
	// logger.error(e.getMessage());//logger.error(e.getMessage(), e);
	// resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
	// }
	// resp.setMessage(answerEntity);
	// return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	// }

	@RequestMapping(value = "/api/answer/reward/{answer}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "reward answer", notes = "reward answer/response ")
	public ResponseEntity<TellnowResponse> reward(@PathVariable("answer") String answerPublicId) {

		TellnowResponse resp = new TellnowResponse();
		boolean answerEntity = false;
		try {
			answerEntity = answerService.reward(answerPublicId, null, Status.rewarded, true);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answerEntity);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/answer/search/{text}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "search answer", notes = "search answer/response ")
	public ResponseEntity<TellnowResponse> search(@PathVariable("text") String text) {

		TellnowResponse resp = new TellnowResponse();
		List<Answer> answers = null;
		try {
			answers = answerService.search(text);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answers);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/answer/search/{field}/{value}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiIgnore
	public ResponseEntity<TellnowResponse> searchTopic(@PathVariable("field") String field, @PathVariable("value") String value) {

		TellnowResponse resp = new TellnowResponse();
		Set<Long> answers = null;
		try {
			answers = answerService.getProfiles(field, value);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(answers);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/answer/rewardpoints", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get reward points", notes = "get all reward points for the user")
	public ResponseEntity<TellnowResponse> getRewardPoints() {

		TellnowResponse resp = new TellnowResponse();
		Double rewardPoints = null;
		try {
			rewardPoints = answerService.getRewardPoints();
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(rewardPoints);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/answer/rewardpoints/{profileId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get reward points", notes = "get all reward points for the user")
	public ResponseEntity<TellnowResponse> getRewardPoints(@PathVariable("profileId") String profileId) {

		TellnowResponse resp = new TellnowResponse();
		Double rewardPoints = null;
		try {
			rewardPoints = answerService.getRewardPoints(profileId);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		resp.setMessage(rewardPoints);
		resp.setRewardPoints(rewardPoints);
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/api/answers/mine/{pageNumber}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "delete answer", notes = "delete and returns the deleted answer ")
	public ResponseEntity<TellnowResponse> getMyAnswers(@PathVariable("pageNumber") Integer pageNumber) {

		TellnowResponse resp = new TellnowResponse();
		Page<Answer> page = answerService.getAnswers(pageNumber, SortAnswersBy.creation_date);
		if (page != null) {
			resp.setMessage(page);
		} else {
			resp.setError(new TellnowError(ErrorCodes.empty_page.getErrorCode(), messages.getMessage(ErrorCodes.empty_page.getErrorMessageCode(), null, Locale.getDefault())));
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
}