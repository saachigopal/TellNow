package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {

}
