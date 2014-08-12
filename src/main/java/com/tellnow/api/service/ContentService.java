package com.tellnow.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tellnow.api.domain.AgeRange;
import com.tellnow.api.domain.Education;
import com.tellnow.api.domain.EducationDiscipline;
import com.tellnow.api.domain.EducationLevel;
import com.tellnow.api.domain.EducationSubDiscipline;
import com.tellnow.api.domain.Religion;
import com.tellnow.api.domain.Topic;

public interface ContentService {

	Page<Topic> getTopics(int pageNumber, int itemsNr, String sortby);

	List<Topic> getAllTopics();

	List<AgeRange> getAllAgeRanges();

	List<Religion> getAllReligions();
	
	List<EducationLevel> getAllEducationLevels();
	
	List<EducationDiscipline> getAllEducationDisciplines();
	
	EducationDiscipline getEducationDiscipline(String disciplineName);
	
	List<EducationSubDiscipline> getAllEducationSubDisciplines();
	
	List<EducationSubDiscipline> getEducationSubDisciplines(String disciplineName);
	
	List<Education> getAllEducation();

	Topic getTopic(Long id);

	Topic addTopic(Topic topic);
}
