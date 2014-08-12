package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.Religion;

public interface ReligionRepository extends JpaRepository<Religion, Long> {

}
