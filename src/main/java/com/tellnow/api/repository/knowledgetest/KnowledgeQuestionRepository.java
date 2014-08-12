package com.tellnow.api.repository.knowledgetest;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.Topic;
import com.tellnow.api.domain.knowledgetest.KnowledgeQuestion;

public interface KnowledgeQuestionRepository extends JpaRepository<KnowledgeQuestion, Long> {

	KnowledgeQuestion findById(Long id);
	
	KnowledgeQuestion findByQuestionPublicId(String questionPublicId);

	@Query(value = "SELECT * FROM knowledge_question ORDER BY RAND() LIMIT 1", nativeQuery = true)
	KnowledgeQuestion randKnowledgeQuestion();

	@Query(value = "SELECT * FROM knowledge_question ORDER BY RAND() LIMIT :howMany", nativeQuery = true)
	Collection<KnowledgeQuestion> randKnowledgeQuestions(@Param("howMany") Long howMany);

	@Query(value = "SELECT * FROM knowledge_question WHERE topicId=:topicId ORDER BY RAND() LIMIT 1", nativeQuery = true)
	KnowledgeQuestion randKnowledgeQuestion(@Param("topicId") Long topicId);

	@Query(value = "SELECT * FROM knowledge_question WHERE topicId=:topicId ORDER BY RAND() LIMIT :howMany", nativeQuery = true)
	Collection<KnowledgeQuestion> randKnowledgeQuestions(@Param("howMany") Long howMany, @Param("topicId") Long topicId);

	@Query(value = "SELECT * FROM knowledge_question WHERE topicId in :topics ORDER BY RAND() LIMIT 1", nativeQuery = true)
	KnowledgeQuestion randKnowledgeQuestion(@Param("topics") long... topicIds);

	@Query(value = "SELECT * FROM knowledge_question WHERE topicId in :topics ORDER BY RAND() LIMIT :howMany", nativeQuery = true)
	Collection<KnowledgeQuestion> randKnowledgeQuestions(@Param("howMany") Long howMany, @Param("topics") Topic... topics);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO reward_points (`profile_id`, `topic_id`, `points`) VALUES (:profileId, :topicId, :value) ON DUPLICATE KEY UPDATE points = points + :value", nativeQuery = true)
	void updateRewardPoints(@Param("profileId") Long profileId, @Param("topicId") Long topicId, @Param("value") Long value);

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
	@Query(value = "INSERT INTO knowledge_question (`creationDate`, `questionPublicId`, `questionText`,`topicId`) VALUES (:creationDate, :questionPublicId, :questionText, :topicId);", nativeQuery = true)
	void insertQuestionNatively(@Param("creationDate") String creationDate, @Param("questionPublicId") String questionPublicId, @Param("questionText") String questionText, @Param("topicId") Long topicId);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO knowledge_question (`id`, `creationDate`, `questionPublicId`, `questionText`,`topicId`) VALUES (:id, :creationDate, :questionPublicId, :questionText, :topicId);", nativeQuery = true)
	void insertQuestionNatively(@Param("id") Long id, @Param("creationDate") String creationDate, @Param("questionPublicId") String questionPublicId, @Param("questionText") String questionText, @Param("topicId") Long topicId);
}
