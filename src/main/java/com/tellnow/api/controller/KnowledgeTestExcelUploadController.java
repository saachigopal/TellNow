package com.tellnow.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.service.impl.MediaServiceImpl;
import com.tellnow.api.utils.knowledgetest.KnowledgeTestExcelHandler;
import com.tellnow.api.utils.thumbnails.ThumbnailGeneratorEngine;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "File upload API", description = "File upload controller")
public class KnowledgeTestExcelUploadController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MediaServiceImpl mediaService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private ThumbnailGeneratorEngine thumbnailGeneratorEngine;

	private static final String MESSAGE_NOT_UPLOADED = "The following cells have errors. Consequently, the corresponding rows were not uploaded.";
	
	@RequestMapping(value = "/api/knowledge/upload", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "informations")
	public String provideUploadInfo() {
		return "You can upload an excel file for populating the questions by posting to this same URL.";
	}

	@RequestMapping(value = "/api/knowledge/upload", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "upload", notes = "handle file upload")
	public ResponseEntity<TellnowResponse> handleFileUpload(@RequestParam("file") MultipartFile file) {
		TellnowResponse resp = new TellnowResponse();

		if (!file.isEmpty()) {
			try {
				InputStream inputStream = file.getInputStream();
				KnowledgeTestExcelHandler excelHandler = new KnowledgeTestExcelHandler(inputStream);
				Map<String, Map<String, LinkedHashMap<Integer, List<Integer>>>> msg = new LinkedHashMap<String, Map<String,LinkedHashMap<Integer,List<Integer>>>>();
				msg.put(MESSAGE_NOT_UPLOADED, excelHandler.parseExcelFile());
				resp.setMessage(msg);
			} catch (IOException e) {
			} finally {
				
			}
			return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		} else {
			resp.setError(new TellnowError(GlobalErrorCodes.bad_request.getErrorCode(), messages.getMessage(GlobalErrorCodes.bad_request.getErrorMessageCode(), null, Locale.getDefault())));
			return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		}
	}

}