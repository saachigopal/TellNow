package com.tellnow.api.service.impl.knowledgetest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.domain.knowledgetest.KnowledgeResponse;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.repository.knowledgetest.KnowledgeAnswerRepository;
import com.tellnow.api.repository.knowledgetest.KnowledgeQuestionRepository;
import com.tellnow.api.repository.knowledgetest.KnowledgeResponseRepository;
import com.tellnow.api.repository.knowledgetest.KnowledgeTestRepository;
import com.tellnow.api.service.knowledgetest.KnowledgeAnswerService;
import com.tellnow.api.service.knowledgetest.KnowledgeQuestionService;
import com.tellnow.api.service.knowledgetest.KnowledgeTestService;

@Service
public class KnowledgeTestServiceImpl implements KnowledgeTestService {

	@Autowired
	KnowledgeAnswerService answerService;

	@Autowired
	KnowledgeAnswerRepository answerRepository;
	
	@Autowired
	KnowledgeQuestionService questionService;
	
	@Autowired
	KnowledgeTestRepository testRepository;

	@Autowired
	KnowledgeResponseRepository responseRepository;
	
	@Autowired
	KnowledgeQuestionRepository questionRepository;

	@Override
	public KnowledgeTest create(int numberOfQuestions) {
		KnowledgeTest test = new KnowledgeTest();
		return test;
	}

	@Override
	public KnowledgeTest create(TellnowProfile profile, int numberOfQuestions) throws TellnowServiceException {
		KnowledgeTest test = new KnowledgeTest(profile);
		testRepository.save(test);
		Collection<KnowledgeQuestion> questions = questionService.getKnowledgeQuestionsByRandom(numberOfQuestions);
		if(questions!=null && !questions.isEmpty()){
			for(KnowledgeQuestion question : questions){
				KnowledgeResponse response = new KnowledgeResponse(question);
				test.addResponse(response);
				questionRepository.save(question);
				responseRepository.save(response);
			}
		}
		return test;
	}
	
	@Override
	public KnowledgeTest create(TellnowProfile profile, int numberOfQuestions, int numberOfAnswersPerQuestion) throws TellnowServiceException {
		KnowledgeTest test = new KnowledgeTest(profile);
		testRepository.save(test);
		Collection<KnowledgeQuestion> questions = questionService.getKnowledgeQuestionsByRandom(numberOfQuestions);
		if(questions!=null && !questions.isEmpty()){
			for(KnowledgeQuestion question : questions){
				KnowledgeResponse response = new KnowledgeResponse(question); 
				Collection<KnowledgeAnswer> answers = answerService.getKnowledgeAnswersByRandom(question.getId(), numberOfAnswersPerQuestion);
				if(answers!=null && !answers.isEmpty()){
					for(KnowledgeAnswer answer : answers){
						test.addAnswer(answer);
						answerRepository.save(answer);
					}
				}
				test.addResponse(response);
				questionRepository.save(question);
				responseRepository.save(response);
			}
		}
		return test;
	}
}
