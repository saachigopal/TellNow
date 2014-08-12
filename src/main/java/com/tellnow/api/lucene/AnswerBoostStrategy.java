package com.tellnow.api.lucene;

import org.hibernate.search.engine.BoostStrategy;

import com.tellnow.api.domain.Answer;
import com.tellnow.api.domain.Answer.Status;

public class AnswerBoostStrategy implements BoostStrategy {

	@Override
	public float defineBoost(Object value) {

		Answer answer = (Answer) value;
		if (answer.getStatus().equals(Status.rewarded)) {
			return 2.0f;
		}
		return 1.0f;
	}

}
