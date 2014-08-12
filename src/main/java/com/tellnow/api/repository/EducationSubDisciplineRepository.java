package com.tellnow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.EducationDiscipline;
import com.tellnow.api.domain.EducationSubDiscipline;

public interface EducationSubDisciplineRepository extends JpaRepository<EducationSubDiscipline, Long> {
	EducationSubDiscipline findBySubdiscipline(String subDisciplineName);
	List<EducationSubDiscipline> findByDiscipline(EducationDiscipline disciplineName);
}
