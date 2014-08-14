package com.tellnow.api.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tellnow.api.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	Question findByQuestionPublicId(String questionPublicId);

	@Query("SELECT 1 FROM Question q WHERE q.questionPublicId = :publicId")
	Integer existsByQuestionPublicId(@Param("publicId") String publicId);

	@Query("SELECT q FROM Question q WHERE status != -1")
	List<Question> getQuestions();

	@Query("SELECT q FROM Question q WHERE status != -1")
	Page<Question> getQuestions(Pageable pageable);

	@Query("SELECT q FROM Question q WHERE status != -1 AND q.owner.id = :ownerId")
	Page<Question> getQuestions(@Param("ownerId") Long ownerId, Pageable pageable);
 
	@Query("SELECT q FROM Question q WHERE q.id NOT IN (SELECT a.id.questionId FROM AnswerReward a) AND q.id IN :topicIds")
	Page<Question> getQuestionsWithoutReward(@Param("topicIds") Set<Long> topicIds, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.id NOT IN (SELECT a.id.questionId FROM AnswerReward a) AND q.id IN :topicIds")
	List<Question> getQuestionsWithoutReward(@Param("topicIds") Set<Long> topicIds);
	
	@Query("SELECT q FROM Question q WHERE q.creationDate <  :creationDate")
	Set<Question> getQuestionsOlderThan(@Param("creationDate") Date creationDate);
}
