package com.tellnow.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.service.CleanerService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Version", description = "Version")
public class CleanController {

	@Autowired
	CleanerService cs;

	@RequestMapping(value = "/api/clean", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get question", notes = "return ")
	public String getProfile() {
		cs.clean("manual");
		return "OK";
	}

}