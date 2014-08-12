package com.tellnow.api.service.knowledgetest;

import java.util.Collection;

import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.exceptions.TellnowServiceException;

public interface KnowledgeAnswerService {

	KnowledgeAnswer getKnowledgeAnswerById(long id) throws TellnowServiceException;
	
	KnowledgeAnswer getKnowledgeAnswerByPublicId(String publicId) throws TellnowServiceException;
	
	KnowledgeAnswer getKnowledgeAnswerByRandom(long questionId) throws TellnowServiceException;
	
	Collection<KnowledgeAnswer> getKnowledgeAnswersByRandom(long questionId, long howMany) throws TellnowServiceException;
}
