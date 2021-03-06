package com.tellnow.api.service;

import java.util.Date;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.tellnow.api.domain.Question;
import com.tellnow.api.exceptions.AnonymousQuestionException;
import com.tellnow.api.exceptions.DuplicateQuestionException;
import com.tellnow.api.question.SortQuestionsBy;

public interface QuestionService {

	Question getQuestion(Long id);

	Question getQuestion(String questionPublicId);
	
	Set<Question> getQuestionsOlderThan(Date date);

	Page<Question> getQuestions(Integer pageNumber, SortQuestionsBy sortBy);

	Page<Question> getAllMyQuestions(Integer pageNumber, SortQuestionsBy sortBy);
	
	Question askaQuestion();

	Question update(Question question, boolean create) throws DuplicateQuestionException, AnonymousQuestionException;

	Question delete(Long id);

	boolean delete(Question question);
	
	boolean delete(Set<Question> questions);
	
	int deleteQuestionsOlderThan(Date date);
}
