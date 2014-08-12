package com.tellnow.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tellnow.api.domain.AgeRange;
import com.tellnow.api.domain.Education;
import com.tellnow.api.domain.EducationDiscipline;
import com.tellnow.api.domain.EducationLevel;
import com.tellnow.api.domain.EducationSubDiscipline;
import com.tellnow.api.domain.Religion;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.repository.AgeRangeRepository;
import com.tellnow.api.repository.EducationDisciplineRepository;
import com.tellnow.api.repository.EducationLevelRepository;
import com.tellnow.api.repository.EducationRepository;
import com.tellnow.api.repository.EducationSubDisciplineRepository;
import com.tellnow.api.repository.ReligionRepository;
import com.tellnow.api.repository.TopicsRepository;
import com.tellnow.api.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TopicsRepository topicsRepository;

	@Autowired
	private AgeRangeRepository agerangeRepository;

	@Autowired
	private EducationRepository educationRepository;

	@Autowired
	private ReligionRepository religionRepository;

	@Autowired
	private EducationLevelRepository educationLevelRepository;

	@Autowired
	private EducationDisciplineRepository educationDisciplineRepository;

	@Autowired
	private EducationSubDisciplineRepository educationSubDisciplineRepository;

	@Override
	public Page<Topic> getTopics(int pageNumber, int itemsNr, String sortby) {
		PageRequest request = new PageRequest(pageNumber, itemsNr, Sort.Direction.DESC, sortby);
		return topicsRepository.findAll(request);
	}

	@Override
	public List<Topic> getAllTopics() {
		return topicsRepository.findAll();
	}

	@Override
	public List<AgeRange> getAllAgeRanges() {
		return agerangeRepository.findAll();
	}

	@Override
	public List<EducationLevel> getAllEducationLevels() {
		return educationLevelRepository.findAll();
	}

	@Override
	public List<EducationDiscipline> getAllEducationDisciplines() {
		return educationDisciplineRepository.findAll();
	}

	@Override
	public EducationDiscipline getEducationDiscipline(String disciplineName) {
		return educationDisciplineRepository.findByDiscipline(disciplineName);
	}

	@Override
	public List<EducationSubDiscipline> getAllEducationSubDisciplines() {
		return educationSubDisciplineRepository.findAll();
	}

	@Override
	public List<EducationSubDiscipline> getEducationSubDisciplines(String disciplineName) {
		EducationDiscipline educationDiscipline = educationDisciplineRepository.findByDiscipline(disciplineName);
		if (educationDiscipline != null) {
			List<EducationSubDiscipline> subDisciplines = educationSubDisciplineRepository.findByDiscipline(educationDiscipline);
			return subDisciplines;
		} else {
			return null;
		}
	}

	@Override
	public List<Religion> getAllReligions() {
		return religionRepository.findAll();
	}

	@Override
	public List<Education> getAllEducation() {
		return educationRepository.findAll();
	}

	@Override
	public Topic getTopic(Long id) {
		return topicsRepository.getOne(id);
	}

	@Override
	public Topic addTopic(Topic topic) {
		return topicsRepository.save(topic);
	}
}
