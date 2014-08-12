package com.tellnow.api.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.EducationDiscipline;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.service.impl.ContentServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Content API", description = "Content provider - selects, list, values")
public class ContentController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messages;

	@Autowired
	ContentServiceImpl contentService;

	@RequestMapping(value = "/api/age-ranges", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get age ranges", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllAgeRanges() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllAgeRanges());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/education-levels", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get education levels", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllEducationLevels() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllEducationLevels());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/education-disciplines", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get all education disciplines", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllEducationDisciplines() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllEducationDisciplines());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/education-disciplines/{discipline}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get one education discipline", notes = "return ")
	public ResponseEntity<TellnowResponse> getOneEducationDiscipline(@PathVariable("discipline") String discipline) {
		if (discipline != null) {
			EducationDiscipline dsc = contentService.getEducationDiscipline(discipline);
			if (dsc == null) {
				int errorCode = GlobalErrorCodes.not_found.getErrorCode();
				String errorMessage = messages.getMessage(GlobalErrorCodes.not_found.getErrorMessageCode(), null, Locale.getDefault());
				TellnowError error = new TellnowError(errorCode, errorMessage);
				TellnowResponse resp = new TellnowResponse(error, null);
				return new ResponseEntity<TellnowResponse>(resp, HttpStatus.NOT_FOUND);
			} else {
				TellnowResponse resp = new TellnowResponse(null, dsc);
				return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
			}
		} else {
			TellnowResponse resp = new TellnowResponse(null, contentService.getAllEducationDisciplines());
			return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/education-subdisciplines", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get education subdisciplines", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllEducationSubDisciplines() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllEducationSubDisciplines());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/religions", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get religions", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllReligions() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllReligions());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/topics", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get topics", notes = "return ")
	public ResponseEntity<TellnowResponse> getAllTopics() {
		TellnowResponse resp = new TellnowResponse(null, contentService.getAllTopics());
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/topics/page/{pageNr}/{itemsNr}/{sortby}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get question", notes = "return ")
	public ResponseEntity<TellnowResponse> getTopics(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("itemsNr") Integer itemsNr, @PathVariable("sortby") String sortby) {

		TellnowResponse resp = new TellnowResponse(null, contentService.getTopics(pageNumber, itemsNr, sortby));
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/topic", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "add topic", notes = "create a new topic ")
	public ResponseEntity<TellnowResponse> addTopic(@RequestBody Topic topic) {
		TellnowResponse resp = null;
		try {
			resp = new TellnowResponse(null, contentService.addTopic(topic));
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			resp = new TellnowResponse(new TellnowError(GlobalErrorCodes.internal_server_error.getErrorCode(), messages.getMessage(GlobalErrorCodes.internal_server_error.getErrorMessageCode(), null, Locale.getDefault())), null);
		}
		return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
	}
}