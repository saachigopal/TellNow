package com.tellnow.api.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopScoredTopics {
	Logger logger = LoggerFactory.getLogger(getClass());
	private Long topicId;

	private Double points;

	public TopScoredTopics(Object[] tstObj) {
		logger.info((Long) tstObj[0] + " - " + (Double) tstObj[1]);
		this.topicId = (Long) tstObj[0];
		this.points = (Double) tstObj[1];
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
}
