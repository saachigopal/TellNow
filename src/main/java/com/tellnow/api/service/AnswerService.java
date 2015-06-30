package com.tellnow.api.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Answer.Status;
import com.tellnow.api.domain.Question;
import com.tellnow.api.exceptions.DuplicateAnswerException;
import com.tellnow.api.exceptions.InvalidDeviceException;
import com.tellnow.api.exceptions.QuestionAnswerLimitException;
import com.tellnow.api.exceptions.QuestionExpiredException;
import com.tellnow.api.exceptions.QuestionMissingException;
import com.tellnow.api.question.SortAnswersBy;

public interface AnswerService {

	Answer getAnswer(Long id);

	Answer getAnswer(String publicId);

	List<Answer> getAnswers(String questionPublicId);

	List<Answer> getAnswers(Long questionId);

	List<Answer> getAnswers(Question question);
	
	Page<Answer> getAnswers(Integer pageNumber, SortAnswersBy creationDate);

	Answer addAnswer(Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException, MalformedTokenStringException;

	Answer addAnswer(Long questionId, Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException, MalformedTokenStringException;

	Answer addAnswer(Question question, Answer answer) throws QuestionMissingException, QuestionExpiredException, QuestionAnswerLimitException, InvalidDeviceException, DuplicateAnswerException, MalformedTokenStringException;

	Answer delete(String publicId);

	Answer delete(Long id);

	boolean delete(Answer answer);

	boolean reward(Answer answer, Integer rewardpoints, Status status,  boolean feedbackNotif) throws InvalidDeviceException, MalformedTokenStringException;

	boolean reward(String answerPublicId, Integer rewardpoints, Status status,  boolean feedbackNotif) throws InvalidDeviceException, MalformedTokenStringException;

	Double getRewardPoints();
	
	Double getRewardPoints(String profileId);

	boolean reject(Answer answer);

	boolean reject(String answerPublicId);

	List<Answer> search(String text);

	Set<Long> getProfiles(String text, String topic);
}
