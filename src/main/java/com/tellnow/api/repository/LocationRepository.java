package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
