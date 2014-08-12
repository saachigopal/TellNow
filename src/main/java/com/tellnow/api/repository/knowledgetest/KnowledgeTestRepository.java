package com.tellnow.api.repository.knowledgetest;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.knowledgetest.KnowledgeTest;

public interface KnowledgeTestRepository extends JpaRepository<KnowledgeTest, Long> {

	KnowledgeTest findById(Long id);
	
	KnowledgeTest findByTestId(String testId);
	
	Collection<KnowledgeTest> findByProfile(TellnowProfile profile);
}
