package com.tellnow.api.service;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.VerificationToken;
import com.tellnow.api.security.AuthUserDetails;

public interface AuthService {

	boolean login(VerificationToken token);

	void logout();

	VerificationToken validateAuthenticationToken(String token);

	boolean isValid(VerificationToken verificationToken);

	Long getId();

	String getUsername();

	TellnowProfile getLoggedInUser();

	AuthUserDetails getLoggedInUserDetails();

	AuthUserDetails getAuthoritiesForUser(String profileID);
}
