package com.tellnow.api.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Answer.Status;
import com.tellnow.api.domain.AnswerReward;
import com.tellnow.api.domain.AnswerRewardKey;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.exceptions.DuplicateAnswerException;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.exceptions.QuestionAnswerLimitException;
import com.tellnow.api.exceptions.QuestionExpiredException;
import com.tellnow.api.exceptions.QuestionMissingException;
import com.tellnow.api.question.SortAnswersBy;
import com.tellnow.api.repository.AnswerRepository;
import com.tellnow.api.repository.AnswerRewardRepository;
import com.tellnow.api.repository.RewardPointsRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.AnswerService;
import com.tellnow.api.service.PushNotificationsService;
//import com.tellnow.api.service.PushNotificationsService;
import com.tellnow.api.utils.TellNowUtils;

@Service
@SuppressWarnings({ "unused", "unchecked" })
public class AnswerServiceImpl implements AnswerService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${answers.page.items.number}")
	private int itemsPerPage;
	
	@Value("${answers.default.maxAnswersAllowed}")
	private int maxAnswersAllowed;

	@Value("${question.answer.default.points.value}")
	private Integer defaultPoints;// Each response gets a user 10 points.

	@Value("${question.answer.rewarded.points.value}")
	private Integer rewardedAnswerPoints;// Each response with positive feedback gets a user 40 points (total 10+40= 50 points)

	@Autowired
	private QuestionServiceImpl questionService;

	@Autowired
	private TellnowProfileServiceImpl profileService;

	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private PushNotificationsService pushNotificationsService;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private AnswerRewardRepository answerRewardRepository;

	@Autowired
	private RewardPointsRepository rewardPointsRepository;

	private EntityManager entityManager;

	private EntityManagerFactory entityManagerFactory;

	@Override
	public Answer getAnswer(Long id) {

		return answerRepository.getOne(id);
	}

	@Override
	public Answer getAnswer(String publicId) {
		return answerRepository.findByPublicId(publicId);
	}

	@Override
	public List<Answer> getAnswers(Question question) {

		return answerRepository.findByQuestion(question);
	}

	@Override
	public List<Answer> getAnswers(String questionPublicId) {
		Question question = questionService.getQuestion(questionPublicId);
		return answerRepository.findByQuestion(question);
	}

	@Override
	public List<Answer> getAnswers(Long questionId) {

		Question question = new Question();
		question.setId(questionId);
		return getAnswers(question);
	}

	@Override
	public Answer addAnswer(Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException{

		if (answer.getQuestion() == null) {
			throw new QuestionMissingException("Missing question", 0);
		}

		if (answer.getQuestion().getId() == null) {
			if (answer.getQuestion().getQuestionPublicId() != null) {
				Question question = questionService.getQuestion(answer.getQuestion().getQuestionPublicId());
				if (question == null) {
					throw new QuestionMissingException("Missing question", 0);
				}
				answer.setQuestion(question);
			}
		}
		if (answer.getQuestion().hasExpired()) {
			throw new QuestionExpiredException("Expired question", 0);
		}

		int max = answer.getQuestion().getMaxAnswersAllowed() != null ? answer.getQuestion().getMaxAnswersAllowed() : maxAnswersAllowed;
		if (answer.getQuestion().getAnswers() != null && answer.getQuestion().getAnswers().size() >= max) {
			throw new QuestionAnswerLimitException("Answer limit reached question", 0);
		}
		String pubId = TellNowUtils.getAnswerPublicId(answer.getQuestion().getQuestionPublicId(), answer.getText());
		Integer alreadyExists = answerRepository.existsByAnswerPublicId(pubId);
		if (alreadyExists != null && alreadyExists > 0) {
			throw new DuplicateAnswerException("Already answered with same text", 0);
		}
		answer.setCreationDate(new Date());
		answer.setOwner(authService.getLoggedInUser());
		answer.setPublicId(pubId);
		answer.setStatus(Status.pendinng);

		try {
			answer = answerRepository.save(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		profileService.incAnswerCounter(answer.getCreationDate());
		if (!answer.isAnonymous()) {
			reward(answer, defaultPoints, null, false);

		}
		pushNotificationsService.notifyAnswer(answer);
		return answer;
	}

	@Override
	public Answer addAnswer(Long questionId, Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException {

		Question question = new Question();
		question.setId(questionId);
		answer.setQuestion(question);
		return addAnswer(answer);
	}

	@Override
	public Answer addAnswer(Question question, Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException {
		answer.setQuestion(question);
		return addAnswer(answer);
	}

	@Override
	public Answer delete(Long id) {

		Answer answer = getAnswer(id);
		if (answer != null) {
			answerRepository.delete(id);
		}
		return answer;
	}

	@Override
	public boolean delete(Answer answer) {

		try {
			answerRepository.delete(answer);
		} catch (Exception e) {
			logger.error(e.getMessage());//logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public Answer delete(String publicId) {
		Answer answer = getAnswer(publicId);
		delete(answer);
		return answer;
	}

	@Override
	public boolean reward(String publicId, Integer rewardpoints, Status status, boolean feedbackNotif) throws InvalidDeviceException {

		Answer answer = getAnswer(publicId);
		// hard coded the value - request by project manager
		return reward(answer, rewardedAnswerPoints, status, feedbackNotif);
	}

	@Override
	public boolean reward(Answer answer, Integer rewardpoints, Status status, boolean feedbackNotif) throws InvalidDeviceException {

		AnswerReward reward = new AnswerReward();
		AnswerRewardKey id = new AnswerRewardKey();
		id.setAnswerId(answer.getId());
		id.setQuestionId(answer.getQuestion().getId());
		id.setProfileId(answer.getOwner().getId());
		id.setTopicId(answer.getQuestion().getTopic().getId());

		reward.setId(id);
		reward.setCreated(new Date());
		reward.setValue(rewardpoints.doubleValue());

		answerRewardRepository.updateAnswerRewardPoints(answer.getId(), answer.getOwner().getId(), answer.getQuestion().getId(), answer.getQuestion().getTopic().getId(), new Date(), rewardpoints.longValue());

		// if the responder of the question was rewarded, add it as interested in question's topic
		if (feedbackNotif == true) {
			TellnowProfile prof = answer.getOwner();
			prof.addInterest(answer.getQuestion().getTopic());
			profileService.update(prof, false);
		}

		if (status != null) {
			answerRepository.updateStatus(answer.getId(), status.getValue());
		}

		profileService.incRewardedAnswerCounter(reward.getCreated());

		// As specified in a message dated on 21.05.2014, the interests should be added
		// only when the responder is rewarded. See above.
		// ------------------------------------------------------------------------------
		// TellnowProfile profile = authService.getLoggedInUser();
		// if (profile.getInterests() != null) {
		// profile.getInterests().add(answer.getQuestion().getTopic());
		// }
		// profileService.update(profile, false);
		if (feedbackNotif == true) {
			pushNotificationsService.notifyRewardPoints(reward);
		}
		return true;
	}

	@Override
	public boolean reject(Answer answer) {

		answerRepository.updateStatus(answer.getId(), Status.rejected.getValue());
		return true;
	}

	@Override
	public boolean reject(String answerPublicId) {

		return true;
	}

	@PersistenceContext
	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public void updateFullTextIndex() throws Exception {
		logger.info("Updating Index");
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		fullTextEntityManager.createIndexer().startAndWait();
	}

	@Override
	public List<Answer> search(String text) {
		EntityManager em = entityManagerFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Answer.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("text").matching(text).createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Answer.class);

		// execute search
		List<Answer> result = jpaQuery.getResultList();
		em.getTransaction().commit();
		em.close();

		return result;
	}

	@Override
	public Set<Long> getProfiles(String text, String topic) {

		EntityManager em = entityManagerFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Answer.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("question.topic.name").matching(topic).createQuery();
		Date date = new DateTime().minusYears(1).toDate();
		Query luceneQuery2 = qb.bool().must(qb.keyword().onField("question.topic.name").matching(topic).createQuery()).must(qb.range().onField("creationDate").above(date).createQuery()).createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery2, Answer.class);

		// execute search
		List<Answer> result = jpaQuery.getResultList();
		Set<Long> ids = new HashSet<Long>();
		for (Iterator<Answer> iterator = result.iterator(); iterator.hasNext();) {
			Answer answer = (Answer) iterator.next();
			ids.add(answer.getOwner().getId());
		}
		em.getTransaction().commit();
		em.close();
		// System.out.println("AnswerServiceImpl.getProfiles()" + ids);
		return ids;
	}

	public Double getRewardPoints() {
		if (authService.getId() == null) {
			return 0d;
		}
		return rewardPointsRepository.getAllRewardPoints(authService.getId());
	}

	public Double getRewardPoints(String profileId) {
		return rewardPointsRepository.getAllRewardPoints(Long.valueOf(profileService.getProfile(profileId).getId()));
	}

	public TellnowProfile getOwner(String reportedEntity) {
		return answerRepository.findByPublicId(reportedEntity).getOwner();
	}

	public Page<Answer> getAnswers(Integer pageNumber, SortAnswersBy sortBy) {
		PageRequest request = new PageRequest(pageNumber, itemsPerPage, Sort.Direction.DESC, sortBy.getField());
		return answerRepository.getAnswers(authService.getLoggedInUser().getId(), request);
	}

}
