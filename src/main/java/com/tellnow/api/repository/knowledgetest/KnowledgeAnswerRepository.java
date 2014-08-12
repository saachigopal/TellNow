package com.tellnow.api.repository.knowledgetest;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.knowledgetest.KnowledgeAnswer;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;

public interface KnowledgeAnswerRepository extends JpaRepository<KnowledgeAnswer, Long> {

	KnowledgeAnswer findById(Long id);
	
	KnowledgeAnswer findByPublicId(String publicId);

	Collection<KnowledgeAnswer> findByQuestion(KnowledgeQuestion question);
	
	Collection<KnowledgeAnswer> findByQuestionAndGood(KnowledgeQuestion question, Boolean good);
	
	@Query(value = "SELECT * FROM knowledge_answer WHERE question_id=:questionId ORDER BY RAND() LIMIT 1", nativeQuery = true)
	KnowledgeAnswer randKnowledgeAnswer(@Param("questionId") Long questionId);

	@Query(value = "SELECT * FROM knowledge_answer WHERE question_id=:questionId ORDER BY RAND() LIMIT :howMany", nativeQuery = true)
	Collection<KnowledgeAnswer> randKnowledgeAnswers(@Param("questionId") Long questionId, @Param("howMany") Long howMany);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_question", nativeQuery = true)
	void emptyQuestionsNatively();

	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_answer", nativeQuery = true)
	void emptyAnswersNatively();

	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_response", nativeQuery = true)
	void emptyResponsesNatively();

	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_answers_test", nativeQuery = true)
	void emptyAnswersTestNatively();

	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_answer_response", nativeQuery = true)
	void emptyAnswersResponsesNatively();

	@Modifying
	@Transactional
	@Query(value = "DELETE from knowledge_answer_media", nativeQuery = true)
	void emptyAnswersMediaNatively();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO knowledge_answer (`creationDate`, `good_or_bad`, `publicId`,`text`, `question_id`) VALUES (:creationDate, :goodOrBad, :publicId, :text, :questionId);", nativeQuery = true)
	void insertAnswerNatively(@Param("creationDate") String creationDate, @Param("goodOrBad") Boolean goodOrBad, @Param("publicId") String publicId, @Param("text") String text, @Param("questionId") Long questionId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO knowledge_answer (`id`, `creationDate`, `good_or_bad`, `publicId`,`text`, `question_id`) VALUES (:id, :creationDate, :goodOrBad, :publicId, :text, :questionId);", nativeQuery = true)
	void insertAnswerNatively(@Param("id") Long id, @Param("creationDate") String creationDate, @Param("goodOrBad") Boolean goodOrBad, @Param("publicId") String publicId, @Param("text") String text, @Param("questionId") Long questionId);
}
