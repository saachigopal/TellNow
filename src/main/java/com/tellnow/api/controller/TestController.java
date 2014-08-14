package com.tellnow.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Chat;
import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.domain.VerificationToken;
import com.tellnow.api.exceptions.AnonymousQuestionException;
import com.tellnow.api.exceptions.DuplicateAnswerException;
import com.tellnow.api.exceptions.DuplicateQuestionException;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.exceptions.QuestionAnswerLimitException;
import com.tellnow.api.exceptions.QuestionExpiredException;
import com.tellnow.api.exceptions.QuestionMissingException;
import com.tellnow.api.repository.AnswerRepository;
import com.tellnow.api.repository.ChatRepository;
import com.tellnow.api.repository.MediaFileRepository;
import com.tellnow.api.repository.VerificationTokenRepository;
import com.tellnow.api.service.AnswerService;
import com.tellnow.api.service.AuthService;
import com.tellnow.api.service.QuestionService;
import com.tellnow.api.service.TellnowProfileService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Test", description = "Test")
public class TestController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	VerificationTokenRepository tokenRepository;

	@Autowired
	TellnowProfileService profileService;
	
	@Autowired
	QuestionService qs;
	
	@Autowired
	AnswerService answerService;

	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	ChatRepository chatRepository;
	
	@Autowired
	MediaFileRepository mfr;

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/api/test", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "get question", notes = "return ")
	public String getProfile() throws 
		JsonProcessingException, 
		DuplicateQuestionException, 
		AnonymousQuestionException, 
		QuestionMissingException, 
		QuestionExpiredException, 
		QuestionAnswerLimitException, 
		InvalidDeviceException, 
		DuplicateAnswerException {
		
		VerificationToken vtoken = tokenRepository.findByToken("a7287170-773f-4c5f-bb47-04eae430cf25");
		authService.login(vtoken);
		
		MediaFile mf1 = new MediaFile();
		mf1.setName("video.wmv");
		mf1.setMd5Sum("012345678909876543210-0001");
		mf1.setOwner("6bfc9035a7ec4533afa94888e8daff5e");
		mf1.setPath("/resources/data/video11.wmv");
		mf1.setUrl("\\resources\\data\\video1.wmv");
		mf1.setContentType("jpeg");
		mfr.saveAndFlush(mf1);
		
		MediaFile mf2 = new MediaFile();
		mf2.setName("video.wmv");
		mf2.setMd5Sum("012345678909876543210-0002");
		mf2.setOwner("6bfc9035a7ec4533afa94888e8daff5e");
		mf2.setPath("/resources/data/video12.wmv");
		mf2.setUrl("\\resources\\data\\video2.wmv");
		mf2.setContentType("wmv");
		mfr.saveAndFlush(mf2);
		
		Topic t = new Topic();
		t.setId(5L);
		t.setName("Animals");
		
		TellnowProfile profile1 = profileService.getProfile("6bfc9035a7ec4533afa94888e8daff5e");
		TellnowProfile profile2 = profileService.getProfile("cd34d4e218e7740668653b2a543902ac");
		
		Question q = new Question();
		Date d = new Date(114, 7, 5);
		q.setQuestionPublicId(UUID.randomUUID().toString());
		q.setAnonymous(false);
		q.setOwner(profile1);
		q.setCreationDate(d);
		q.setQuestionText("Question");
		q.setTopic(t);
		List<MediaFile> l = new ArrayList<MediaFile>();
		l.add(mf1);
		l.add(mf2);
		q.setMediaFiles(l);
		qs.update(q, true);
		
		MediaFile mf3 = new MediaFile();
		mf3.setName("video.wmv");
		mf3.setMd5Sum("012345678909876543210-0003");
		mf3.setOwner("cd34d4e218e7740668653b2a543902ac");
		mf3.setPath("/resources/data/video13.wmv");
		mf3.setUrl("\\resources\\data\\video3.wmv");
		mf3.setContentType("wmv");
		mfr.saveAndFlush(mf3);

		Answer a = new Answer();
		a.setCreationDate(d);
		a.setQuestion(q);
		a.setText("Answer");
		a.setPublicId(UUID.randomUUID().toString());
		a.setOwner(profile2);
		Set<MediaFile> s = new HashSet<MediaFile>();
		s.add(mf3);
		a.setMediaFiles(s);
		answerRepository.save(a);
		
		MediaFile mf4 = new MediaFile();
		mf4.setName("video.wmv");
		mf4.setMd5Sum("012345678909876543210-0003");
		mf4.setOwner("cd34d4e218e7740668653b2a543902ac");
		mf4.setPath("/resources/data/video14.wmv");
		mf4.setUrl("\\resources\\data\\video4.wmv");
		mf4.setContentType("wmv");
		mfr.saveAndFlush(mf4);

		s.clear();s.add(mf4);
		Chat c = new Chat(); 
		c.setDate(d);
		c.setSender(profile1);
		c.setSendee(profile2);
		c.setMessage("Ce mai face Avram Iancu?");
		c.setMediaFiles(s);
		chatRepository.save(c);
		//answerService.addAnswer(a);
		ObjectMapper om = new ObjectMapper();
		String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(q);
		return json;
	}

}