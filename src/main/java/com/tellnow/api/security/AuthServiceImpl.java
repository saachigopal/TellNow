package com.tellnow.api.security;

import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tellnow.api.domain.TellnowProfile;
import com.tellnow.api.domain.VerificationToken;
import com.tellnow.api.exceptions.RestException;
import com.tellnow.api.repository.ProfileRepository;
import com.tellnow.api.repository.VerificationTokenRepository;
import com.tellnow.api.service.AuthService;

/**
 * Service for authentication and retrieval of logged in user to be used by other objects.
 */
@Service
public class AuthServiceImpl implements AuthService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	VerificationTokenRepository tokenRepository;

	/**
	 * Will not touch the database
	 * 
	 * @return Id of the currently logged in user
	 */
	public Long getId() {
		return getLoggedInUserDetails() != null ? getLoggedInUserDetails().getId() : null;
	}

	/**
	 * Will not use the database
	 * 
	 * @return Username of the currently logged in user
	 */
	public String getUsername() {
		return getLoggedInUserDetails() != null ? getLoggedInUserDetails().getUsername() : null;
	}

	/**
	 * Try to auth a user based on a token
	 * 
	 * @param token
	 * @return whether the login succeeded or not.
	 */
	public boolean login(VerificationToken token) {
		Authentication authentication = authenticationManager.authenticate(new Token(token));
		boolean isAuthenticated = isAuthenticated(authentication);
		if (isAuthenticated) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		return isAuthenticated;
	}

	/**
	 * Logs out the user
	 */
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Get profile from database
	 */
	public TellnowProfile getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			return profileRepository.findOne(getId());
		}
		return null;
	}

	public AuthUserDetails getLoggedInUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			return (AuthUserDetails) authentication.getPrincipal();
		}
		return null;
	}

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

	public VerificationToken validateAuthenticationToken(String token) {
		VerificationToken vToken = tokenRepository.findByToken(token);
		if (vToken != null) {
			if (vToken.hasExpired()) {
				vToken = null;
			}
		}
		return vToken;
	}

	public boolean isValid(VerificationToken verificationToken) {
		VerificationToken token = tokenRepository.findByToken(verificationToken.getToken());
		// if token is null or is expired then return false
		return ((token == null || token.hasExpired()) ? false : true);
	}

	@Transactional
	public AuthUserDetails getAuthoritiesForUser(String profileID) {

		if (profileID == null) {
			throw new RestException("User not found", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		TellnowProfile profile = profileRepository.findByprofileId(profileID);

		if (profile == null) {
			logger.warn("Couldn't fetch authorities for profile {}, got null", profileID);
			throw new RestException("User not found", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		HashSet<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		// Set<String> permissions = user.getPermissions();
		//
		// HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();
		// if (permissions != null) {
		// for (String permission : permissions) {
		// grantedAuthorities.add(new SimpleGrantedAuthority(permission));
		// }
		// }

		AuthUserDetails authUserDetails = new AuthUserDetails(profile, grantedAuthorities);
		return authUserDetails;
	}
}
