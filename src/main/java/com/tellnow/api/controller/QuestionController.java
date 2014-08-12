package com.tellnow.api.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.Question;
import com.tellnow.api.exceptions.AnonymousQuestionException;
import com.tellnow.api.exceptions.DuplicateQuestionException;
import com.tellnow.api.question.ErrorCodes;
import com.tellnow.api.question.SortQuestionsBy;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.impl.QuestionServiceImpl;
import com.tellnow.api.service.impl.TellnowProfileServiceImpl;
import com.tellnow.api.utils.TellNowUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Questions API", description = "Question related Rest operations")
public class QuestionController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TellnowProfileServiceImpl profileService;

	@Autowired
	private QuestionServiceImpl questionService;

	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private MessageSource messages;

	@RequestMapping(value = "/api/question/{question}", method = RequestMethod.GET)
	@ApiOperation(value = "get question", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestion(@PathVariable("question") String question) {

		TellnowResponse resp = new TellnowResponse();
		Question quest = questionService.getQuestion(question);

		if (quest == null) {
			resp.setError(new TellnowError(ErrorCodes.missing_question.getErrorCode(), messages.getMessage(ErrorCodes.missing_question.getErrorMessageCode(), null, Locale.getDefault())));
		} else {
			resp.setMessage(quest);
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/question/text/{question}", method = RequestMethod.GET)
	@ApiOperation(value = "get question", notes = "return question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestionBy(@PathVariable("question") String question) {

		// get MD5 sum from user identifier
		String questionID = TellNowUtils.getQuestionId(authService.getUsername(), question, false);

		TellnowResponse resp = new TellnowResponse();
		Question quest = questionService.getQuestion(questionID);
		if (quest == null) {
			resp.setError(new TellnowError(ErrorCodes.missing_question.getErrorCode(), messages.getMessage(ErrorCodes.missing_question.getErrorMessageCode(), null, Locale.getDefault())));
		} else {
			resp.setMessage(quest);
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/question", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "create question", notes = "return newly created question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> createQuestion(@RequestBody Question question) {

		logger.info("createQuestion:");
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info("createQuestion:" + mapper.writeValueAsString(question));
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException", e);
		}
		TellnowResponse resp = new TellnowResponse();
		if (question.getQuestionPublicId() == null) {
			String questionID = TellNowUtils.getQuestionId(authService.getUsername(), question.getQuestionText(), false);
			question.setQuestionPublicId(questionID);
		}
		try {
			questionService.update(question, true);
		} catch (AnonymousQuestionException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.anonymous_question.getErrorCode(), messages.getMessage(ErrorCodes.anonymous_question.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (DuplicateQuestionException e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.duplicate_question.getErrorCode(), messages.getMessage(ErrorCodes.duplicate_question.getErrorMessageCode(), null, Locale.getDefault())));
		} catch (Exception e) {
//			logger.error(e.getMessage());
			logger.error(e.getMessage(), e);
			resp.setError(new TellnowError(ErrorCodes.save_error.getErrorCode(), messages.getMessage(ErrorCodes.save_error.getErrorMessageCode(), null, Locale.getDefault())));
		}
		if (question != null) {
			if (question.getOwner() != null) {
				Double rewardPoints = profileService.getAllRewardPoints(question.getOwner());
				resp.setRewardPoints(rewardPoints);
			}
			resp.setMessage(question);
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/question/{question}", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete question", notes = "return deleted question")
	public @ResponseBody
	ResponseEntity<TellnowResponse> deleteQuestion(@PathVariable("question") String questionPublicID) {

		TellnowResponse resp = new TellnowResponse();
		Question question = questionService.getQuestion(questionPublicID);
		if (question != null) {
			try {
				questionService.delete(question.getId());
			} catch (Exception e) {
				logger.debug(e.getMessage(), e);
				resp.setError(new TellnowError(ErrorCodes.delete_error.getErrorCode(), messages.getMessage(ErrorCodes.delete_error.getErrorMessageCode(), null, Locale.getDefault())));
			}
		} else {

			resp.setError(new TellnowError(ErrorCodes.missing_question.getErrorCode(), messages.getMessage(ErrorCodes.missing_question.getErrorMessageCode(), null, Locale.getDefault())));
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/questions/page/{pageNumber}", method = RequestMethod.GET)
	@ApiOperation(value = "get questions", notes = "return questions from the given page")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getQuestions(@PathVariable("pageNumber") Integer pageNumber) {
		TellnowResponse resp = new TellnowResponse();
		Page<Question> page = questionService.getQuestions(pageNumber, SortQuestionsBy.creation_date);

		if (page != null) {
			resp.setMessage(page);
		} else {
			resp.setError(new TellnowError(ErrorCodes.empty_page.getErrorCode(), messages.getMessage(ErrorCodes.empty_page.getErrorMessageCode(), null, Locale.getDefault())));
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/questions/{pageNumber}", method = RequestMethod.GET)
	@ApiOperation(value = "get questions", notes = "return questions from the given page")
	public @ResponseBody
	ResponseEntity<TellnowResponse> getMineQuestions(@PathVariable("pageNumber") Integer pageNumber) {
		TellnowResponse resp = new TellnowResponse();
		Page<Question> page = questionService.getAllMyQuestions(pageNumber, SortQuestionsBy.creation_date);

		if (page != null) {
			resp.setMessage(page);
		} else {
			resp.setError(new TellnowError(ErrorCodes.empty_page.getErrorCode(), messages.getMessage(ErrorCodes.empty_page.getErrorMessageCode(), null, Locale.getDefault())));
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api/askaquestion", method = RequestMethod.GET)
	@ApiOperation(value = "get questions", notes = "return questions from the given page")
	public @ResponseBody
	ResponseEntity<TellnowResponse> askaQuestion() {
		TellnowResponse resp = new TellnowResponse();
		Question page = questionService.askaQuestion();

		if (page != null) {
			resp.setMessage(page);
		} else {
			resp.setError(new TellnowError(ErrorCodes.empty_page.getErrorCode(), messages.getMessage(ErrorCodes.empty_page.getErrorMessageCode(), null, Locale.getDefault())));
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
}