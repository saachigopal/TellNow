package com.tellnow.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tellnow.api.TellnowResponse;
import com.tellnow.api.domain.ReportedEntity;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.impl.ReportingServiceImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Reporting", description = "Report system")
public class ReportingController {
	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private ReportingServiceImpl reportingService;

	@RequestMapping(value = "/api/report/question", method = RequestMethod.POST)
	@ApiOperation(value = "report a question", notes = "with this service you can report an inappropriate question ")
	public ResponseEntity<TellnowResponse> reportingQuestion(@RequestBody ReportedEntity reportedItem) {

		reportedItem.setComplainerId(authService.getId());
		reportedItem.setEntityType(ReportedEntity.EntityType.question);
		reportedItem.setReportingType(ReportedEntity.ReportingType.inappropriate);
		reportingService.reportEntity(reportedItem);

		return new ResponseEntity<TellnowResponse>(new TellnowResponse(reportedItem), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/report/answer", method = RequestMethod.POST)
	@ApiOperation(value = "report an answer", notes = "with this service you can report an inappropriate answer ")
	public ResponseEntity<TellnowResponse> reportingAnswer(@RequestBody ReportedEntity reportedItem) {
		reportedItem.setComplainerId(authService.getId());
		reportedItem.setEntityType(ReportedEntity.EntityType.answer);
		reportedItem.setReportingType(ReportedEntity.ReportingType.inappropriate);
		reportingService.reportEntity(reportedItem);

		return new ResponseEntity<TellnowResponse>(new TellnowResponse(reportedItem), HttpStatus.OK);
	}

}