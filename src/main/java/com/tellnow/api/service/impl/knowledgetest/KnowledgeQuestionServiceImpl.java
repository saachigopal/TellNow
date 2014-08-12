package com.tellnow.api.service.impl.knowledgetest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.repository.knowledgetest.KnowledgeQuestionRepository;
import com.tellnow.api.service.TellnowProfileService;
import com.tellnow.api.service.knowledgetest.KnowledgeQuestionService;

@Service
public class KnowledgeQuestionServiceImpl implements KnowledgeQuestionService{

	@Autowired
	private KnowledgeQuestionRepository knowledgeQuestionRepository;
	
	@Override
	public void updateRewardPoints(long profileId, long topicId, long value) {
		knowledgeQuestionRepository.updateRewardPoints(profileId, topicId, value);
	}
	
	@Override
	public KnowledgeQuestion getKnowledgeQuestionById(long id) throws TellnowServiceException {
		return knowledgeQuestionRepository.findById(id);
	}

	@Override
	public KnowledgeQuestion getKnowledgeQuestionByPublicId(String questionPublicId) throws  TellnowServiceException{
		try {
			return knowledgeQuestionRepository.findByQuestionPublicId(questionPublicId);
		} catch (Exception e) {
			throw new TellnowServiceException(5001, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public KnowledgeQuestion getKnowledgeQuestionByRandom() throws TellnowServiceException {
		try {
			return knowledgeQuestionRepository.randKnowledgeQuestion();
		} catch (Exception e) {
			throw new TellnowServiceException(5002, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany) throws TellnowServiceException {
		try {
			return knowledgeQuestionRepository.randKnowledgeQuestions(howMany);
		} catch (Exception e) {
			throw new TellnowServiceException(5002, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public KnowledgeQuestion getKnowledgeQuestionByRandom(long topicId) throws TellnowServiceException {
		try {
			return knowledgeQuestionRepository.randKnowledgeQuestion(topicId);
		} catch (Exception e) {
			throw new TellnowServiceException(5002, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany, long topicId) throws TellnowServiceException {
		try {
			return knowledgeQuestionRepository.randKnowledgeQuestions(topicId, howMany);
		} catch (Exception e) {
			throw new TellnowServiceException(5002, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public KnowledgeQuestion getKnowledgeQuestionByRandom(long... topicIds) throws TellnowServiceException {
		try {
			return knowledgeQuestionRepository.randKnowledgeQuestion(topicIds);
		} catch (Exception e) {
			throw new TellnowServiceException(5002, "error.message.knowledge.question.invalid.parameters", e);
		}
	}

	@Override
	public Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(long howMany, long... topicId) throws TellnowServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<KnowledgeQuestion> getKnowledgeQuestionsByRandom(TellnowProfileService profile, long howMany) throws TellnowServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}


