package com.tellnow.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Question;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

	List<Answer> findByQuestion(Question question);

	@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND status != -1")
	List<Answer> getAnswersForQuestion(@Param("questionId") String questionId);

	@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND status != -1")
	Page<Answer> getAnswersForQuestion(@Param("questionId") String questionId, Pageable pageable);

	@Query("SELECT a FROM Answer a WHERE a.owner.id = :ownerId AND status != -1")
	Page<Answer> getAnswers(@Param("ownerId") Long ownerId, Pageable pageable);

	Answer findByPublicId(String publicId);

	@Query("SELECT 1 FROM Answer a WHERE a.publicId = :publicId")
	Integer existsByAnswerPublicId(@Param("publicId") String publicId);

	@Modifying
	@Transactional
	@Query("UPDATE Answer a SET a.status = :value WHERE a.id = :id")
	void updateStatus(@Param("id") Long id, @Param("value") Integer value);

}
