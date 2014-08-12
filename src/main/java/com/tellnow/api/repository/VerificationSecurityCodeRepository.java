package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellnow.api.domain.VerificationSecurityCode;

public interface VerificationSecurityCodeRepository extends JpaRepository<VerificationSecurityCode, Long> {

	VerificationSecurityCode findByPhoneNumber(String phoneNumber);
}
