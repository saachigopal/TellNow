package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tellnow.api.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByProfileID(String profileID);

	VerificationToken findByToken(String token);

	@Query("select t from VerificationToken t where t.token = :token")
	String getTokenStatus(VerificationToken verificationToken);
}
