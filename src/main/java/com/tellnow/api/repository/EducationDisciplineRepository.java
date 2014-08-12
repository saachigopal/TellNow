package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.EducationDiscipline;

public interface EducationDisciplineRepository extends JpaRepository<EducationDiscipline, Long> {
	EducationDiscipline findByDiscipline(String disciplineName);
}
