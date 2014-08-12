package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.ReportedEntity;

public interface ReportedEntityRepository extends JpaRepository<ReportedEntity, Long> {

}
