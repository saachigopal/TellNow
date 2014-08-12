package com.tellnow.api.service.knowledgetest;

import java.util.Collection;

import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.service.TellnowProfileService;

public interface KnowledgeQuestionService {
	
	void updateRewardPoints(long profileId, long topicId, long value);

	KnowledgeQuestion getKnowledgeQuestionById(long id) throws TellnowServiceException;
	
	KnowledgeQuestion getKnowledgeQuestionByPublicId(String questionPublicId) throws TellnowServiceException;
	
	KnowledgeQuestion getKnowledgeQuestionByRandom() throws TellnowServiceException;
	
	Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany) throws TellnowServiceException;
	
	KnowledgeQuestion getKnowledgeQuestionByRandom(long topicId) throws TellnowServiceException;
	
	Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany, long topicId) throws TellnowServiceException;
	
	KnowledgeQuestion getKnowledgeQuestionByRandom(long... topicId) throws TellnowServiceException;
	
	Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany, long... topicId) throws TellnowServiceException;
	
	Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(TellnowProfileService profile, long howMany) throws TellnowServiceException;
}
