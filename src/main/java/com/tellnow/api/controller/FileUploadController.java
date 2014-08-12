package com.tellnow.api.controller;

import java.io.File;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
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
import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.exceptions.NotAllowedMediaTypeException;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.service.impl.MediaServiceImpl;
import com.tellnow.api.utils.thumbnails.ThumbnailGeneratorEngine;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "File upload API", description = "File upload controller")
public class FileUploadController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MediaServiceImpl mediaService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private ThumbnailGeneratorEngine thumbnailGeneratorEngine;

	@RequestMapping(value = "/api/upload", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "informations")
	public String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/api/upload", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "upload", notes = "handle file upload")
	public ResponseEntity<TellnowResponse> handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		StringBuilder sb = new StringBuilder("File name: ");
		sb.append(name);
		sb.append("\tfile:").append(file.getName()).append("-").append(file.getContentType());
		logger.info(sb.toString());

		TellnowResponse resp = new TellnowResponse();

		if (!file.isEmpty()) {
			try {

				MediaFile mfile = mediaService.saveFile(name, file, null);
				resp.setMessage(mfile);
				MediaType type = MediaType.parseMediaType(file.getContentType());

				if (type.getType().compareTo("image") == 0 || type.getType().compareTo("video") == 0) {
					File sfile = new File(mfile.getPath());
					thumbnailGeneratorEngine.generateThumbnails(FilenameUtils.getBaseName(sfile.getName()), sfile, mfile.getContentType());
					sfile = null;
				}

				return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);

			} catch (NotAllowedMediaTypeException ex) {
				logger.error(ex.getMessage(), ex);
				resp.setError(new TellnowError(GlobalErrorCodes.not_allowed.getErrorCode(), messages.getMessage(GlobalErrorCodes.not_allowed.getErrorMessageCode(), null, Locale.getDefault())));
				return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
			} catch (Exception e) {
				logger.error(e.getMessage());//logger.error(e.getMessage(), e);
				resp.setError(new TellnowError(GlobalErrorCodes.internal_server_error.getErrorCode(), messages.getMessage(GlobalErrorCodes.internal_server_error.getErrorMessageCode(), null, Locale.getDefault())));
				return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
			}
		} else {
			resp.setError(new TellnowError(GlobalErrorCodes.bad_request.getErrorCode(), messages.getMessage(GlobalErrorCodes.bad_request.getErrorMessageCode(), null, Locale.getDefault())));
			return new ResponseEntity<TellnowResponse>(resp, HttpStatus.OK);
		}
	}

}