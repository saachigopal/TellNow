package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.AgeRange;

public interface AgeRangeRepository extends JpaRepository<AgeRange, Long> {

}
