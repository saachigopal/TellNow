package com.tellnow.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.service.impl.AdminServiceImpl;
import com.tellnow.api.service.impl.AnswerServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Version", description = "Version")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AnswerServiceImpl answerService;
	
	@Autowired
	private AdminServiceImpl adminService;
	
	@RequestMapping(value = "/api/admin/updateindex", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "update indexes", notes = "answer service - update apache lucene indexes")
	public String updateIndexes() {
		
		try {
			answerService.updateFullTextIndex();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/api/admin/candidatestonotify/{nr}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "set candidates to notify number", notes = "")
	public String getCandidatesToNotify(@PathVariable("nr") Integer nr) {
		
		try {
			adminService.userNumber(nr);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/api/admin/getcandidatestonotify", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "set candidates to notify number", notes = "")
	public String candidatesToNotify() {
		
		try {
			return adminService.getUserNumber().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}