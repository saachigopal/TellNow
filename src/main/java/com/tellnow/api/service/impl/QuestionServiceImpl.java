package com.tellnow.api.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.domain.Question;
import com.tellnow.api.domain.RewardPoints;
import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.exceptions.AnonymousQuestionException;
import com.tellnow.api.exceptions.DuplicateQuestionException;
import com.tellnow.api.question.SortQuestionsBy;
import com.tellnow.api.question.TopScoredTopics;
import com.tellnow.api.repository.AnswerRewardRepository;
import com.tellnow.api.repository.QuestionRepository;
import com.tellnow.api.repository.RewardPointsRepository;
import com.tellnow.api.repository.TopicsRepository;
import com.tellnow.api.security.AuthServiceImpl;
import com.tellnow.api.service.MediaService.TellnowMediaType;
import com.tellnow.api.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${question.page.items.number}")
	private int itemsPerPage;

	@Value("${topics.to.search.number}")
	private int topicsNr;

	@Value("${question.anonymous.cost.value}")
	private double anonymousQuestionCost;// An anonymous question costs the user 20 points (deduct 20 points from balance - user cannot ask anonymous question if balance below 20 points)

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private MediaServiceImpl mediaService;

	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private TellnowProfileServiceImpl profileService;

	@Autowired
	private RewardPointsRepository rewardPointsRepository;

	@Autowired
	private AnswerRewardRepository answerRewardRepository;

	@Autowired
	private TopicsRepository topicsRepository;

	@Override
	public Question getQuestion(Long id) {
		return questionRepository.findOne(id);
	}

	@Override
	public Question getQuestion(String questionPublicId) {
		Question question = null;
		try {
			question = questionRepository.findByQuestionPublicId(questionPublicId);
		} catch (NonUniqueResultException e) {
			logger.error(e.getMessage());// logger.error(e.getMessage(), e);
			return question;
		}
		return question;
	}

	@Override
	public Page<Question> getQuestions(Integer pageNumber, SortQuestionsBy sortBy) {

		PageRequest request = new PageRequest(pageNumber, itemsPerPage, Sort.Direction.DESC, sortBy.getField());
		return questionRepository.getQuestions(request);
	}

	@Override
	public Page<Question> getAllMyQuestions(Integer pageNumber, SortQuestionsBy sortBy) {

		PageRequest request = new PageRequest(pageNumber, itemsPerPage, Sort.Direction.DESC, sortBy.getField());
		return questionRepository.getQuestions(authService.getLoggedInUser().getId(), request);
	}

	@Override
	public Question update(Question question, boolean create) throws DuplicateQuestionException, AnonymousQuestionException {

		Double rewardPoints = null;
		if (question.isAnonymous()) {
			rewardPoints = rewardPointsRepository.getAllRewardPoints(authService.getId());
			if (rewardPoints < anonymousQuestionCost) {
				throw new AnonymousQuestionException("You need 20 reward points to ask a question anonymuosly", 0);
			}
		}

		Integer alreadyExists = questionRepository.existsByQuestionPublicId(question.getQuestionPublicId());
		if (alreadyExists != null && alreadyExists > 0) {
			throw new DuplicateQuestionException("Already added the exactly same question", 0);
		}
		if (question.getOwner() == null) {
			question.setOwner(authService.getLoggedInUser());
		}

		if (question.getCreationDate() == null) {
			question.setCreationDate(new Date());
		}
		if (question.getMediaFiles() != null && !question.getMediaFiles().isEmpty()) {
			validateAttachedFiles(question.getMediaFiles());
		}
		try {
			question = questionRepository.saveAndFlush(question);
			profileService.incQuestionCounter(question.getCreationDate());
		} catch (PersistenceException e) {
			logger.error(e.getMessage());// logger.error(e.getMessage(), e);
			question = null;
		} catch (Exception e) {
			// logger.error(e.getMessage());
			logger.error(e.getMessage(), e);
			question = null;
		}
		if (question != null && question.isAnonymous()) {
			decrementThe20points(question.getTopic().getId(), anonymousQuestionCost);
		}

		return question;
	}

	private int decrementThe20points(Long topicId, double anonymousQuestionCost) {
		int rowsAffected = rewardPointsRepository.decrementRewardPoints(authService.getId(), topicId, anonymousQuestionCost);// value is 0 or 1
		// if we don't have points for this topic decrement this points from the one with max points
		if (rowsAffected == 0) {
			logger.info("decrement points from another topic - this is a request I had to  ");
			List<RewardPoints> topicsWithMax = rewardPointsRepository.getTopicWithMaxPoints(authService.getId());
			if (topicsWithMax != null && !topicsWithMax.isEmpty()) {
				rewardPointsRepository.decrementRewardPoints(authService.getId(), topicsWithMax.get(0).getId().getTopicId(), anonymousQuestionCost);// value is 0 or 1
			}
		}
		return rowsAffected;
	}

	private void validateAttachedFiles(Collection<MediaFile> collection) {
		for (Iterator<MediaFile> iterator = collection.iterator(); iterator.hasNext();) {
			MediaFile mediaFile = (MediaFile) iterator.next();
			if (mediaFile.getId() == null) {
				mediaFile.setId(mediaService.getMediaFileId(mediaFile.getMd5Sum()));
			}
		}
	}

	public void update(Question question, MultipartFile imageFile, MultipartFile audioFile, MultipartFile videoFile, boolean create) throws Exception {

		Set<MediaFile> qMedia = new HashSet<MediaFile>();
		if (imageFile != null) {
			MediaFile image = mediaService.saveFile(imageFile.getName(), imageFile, TellnowMediaType.image);
			qMedia.add(image);
		}

		if (audioFile != null) {
			MediaFile audio = mediaService.saveFile(audioFile.getName(), audioFile, TellnowMediaType.audio);
			qMedia.add(audio);
		}

		if (videoFile != null) {
			MediaFile video = mediaService.saveFile(videoFile.getName(), videoFile, TellnowMediaType.video);
			qMedia.add(video);
		}
		if (!qMedia.isEmpty()) {
			if (question.getMediaFiles() != null) {
				question.getMediaFiles().addAll(qMedia);
			} else {
				question.setMediaFiles(qMedia);
			}
		}
		update(question, create);
	}

	@Override
	public Question delete(Long id) {
		Question deletedQuestion;
		try {
			deletedQuestion = getQuestion(id);
			questionRepository.delete(id);
		} catch (Exception e) {
			logger.error(e.getMessage());// logger.error(e.getMessage(), e);
			deletedQuestion = null;
		}
		return deletedQuestion;
	}

	@Override
	public boolean delete(Question question) {
		try {
			questionRepository.delete(question);
		} catch (Exception e) {
			logger.error(e.getMessage());// logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public TellnowProfile getOwner(String reportedEntity) {
		return questionRepository.findByQuestionPublicId(reportedEntity).getOwner();

	}

	public Question askaQuestion() {
		logger.info("askaQuestion");

		Set<Long> topics = getTopicsForUser();

		PageRequest request = new PageRequest(0, topicsNr, Sort.Direction.DESC, SortQuestionsBy.id.getField());

		Page<Question> questions = questionRepository.getQuestionsWithoutReward(topics, request);

		if (questions.getNumber() <= 0) {
			questions = questionRepository.getQuestions(request);
		}

		// pick one question randomly
		Random rand = new Random();
		if (questions.getNumberOfElements() > 1) {
			int n = rand.nextInt(questions.getNumberOfElements()) + 1;
			int i = 0;
			Iterator<Question> it = questions.iterator();
			while (it.hasNext()) {
				Question question = it.next();
				i++;
				if (i == n) {
					return question;
				}
			}
		}
		return null;
	}

	private Set<Long> getTopicsForUser() {
		Set<Long> topics = new HashSet<Long>();
		// first based on answers
		PageRequest request = new PageRequest(0, topicsNr);
		Page<Object[]> topScoredTopics = answerRewardRepository.getUserTopScoredTopics(authService.getLoggedInUser().getId(), request);
		topics = getTopics(topScoredTopics);
		// based on interests
		if (topics.isEmpty()) {
			topics = getTopicIdsBasedOnInterest(authService.getLoggedInUser().getInterests());
		}
		// get all topics
		if (topics.isEmpty()) {
			topics = topicsRepository.getAllTopicIds();
		}
		return topics;
	}

	private Set<Long> getTopicIdsBasedOnInterest(Set<Topic> interests) {
		Set<Long> result = new HashSet<Long>();
		for (Iterator<Topic> iterator = interests.iterator(); iterator.hasNext();) {
			Topic topic = (Topic) iterator.next();
			result.add(topic.getId());
		}

		return result;
	}

	private Set<Long> getTopics(Page<Object[]> myTopTopicsId) {
		Set<Long> result = new HashSet<Long>();
		for (Iterator<Object[]> iterator = myTopTopicsId.getContent().iterator(); iterator.hasNext();) {
			TopScoredTopics tst = new TopScoredTopics(iterator.next());
			result.add(tst.getTopicId());
		}
		return result;
	}
}
