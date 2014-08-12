package com.tellnow.api.service.impl.knowledgetest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.exceptions.TellnowServiceException;
import com.tellnow.api.repository.knowledgetest.KnowledgeAnswerRepository;
import com.tellnow.api.service.knowledgetest.KnowledgeAnswerService;

@Service
public class KnowledgeAnswerServiceImpl implements KnowledgeAnswerService{
	
	@Autowired
	KnowledgeAnswerRepository answerRepository;
	
	@Override
	public KnowledgeAnswer getKnowledgeAnswerById(long id) throws TellnowServiceException {
		return answerRepository.findById(id);
	}

	@Override
	public KnowledgeAnswer getKnowledgeAnswerByPublicId(String publicId) throws TellnowServiceException {
		return answerRepository.findByPublicId(publicId);
	}

	@Override
	public KnowledgeAnswer getKnowledgeAnswerByRandom(long questionId) throws TellnowServiceException {
		return answerRepository.randKnowledgeAnswer(questionId);
	}

	@Override
	public Collection<KnowledgeAnswer> getKnowledgeAnswersByRandom(long questionId, long howMany) throws TellnowServiceException {
		return answerRepository.randKnowledgeAnswers(questionId, howMany);
	}

}
