package com.tellnow.api.service;

public interface CleanerService {
	void clean();
	void clean(String cronExpression);
}
