package com.tellnow.api.repository.knowledgetest;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;
import com.tellnow.api.domain.knowledgetest.KnowledgeResponse;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;

public interface KnowledgeResponseRepository extends JpaRepository<KnowledgeResponse, Long> {

	KnowledgeResponse findById(Long id);
	
	KnowledgeResponse findByPublicId(String publicId);
	
	Collection<KnowledgeResponse> findByQuestion(KnowledgeQuestion question);
	
	Collection<KnowledgeResponse> findByTest(KnowledgeTest test);
}
