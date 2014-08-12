package com.tellnow.api.service.knowledgetest;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;
import com.tellnow.api.exceptions.TellnowServiceException;

public interface KnowledgeTestService {
	
	public KnowledgeTest create(int numberOfQuestions);
	
	public KnowledgeTest create(TellnowProfile profile, int numberOfQuestions) throws TellnowServiceException;
	
	public KnowledgeTest create(TellnowProfile profile, int numberOfQuestions, int numberOfAnswersPerQuestion) throws TellnowServiceException; 
}
