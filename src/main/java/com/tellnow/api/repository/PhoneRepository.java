package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	Phone findByphoneNumber(String phoneNumber);
}
