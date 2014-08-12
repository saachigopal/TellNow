package com.tellnow.api.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.tellnow.api.domain.VerificationToken;

/**
 * Our custom Authentication, will hold the authorities of the user and the authUserDetails.
 */
public class Token extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -5812027338580146664L;

	private VerificationToken vToken;
	private UserDetails principal;

	/**
	 * Standard, non-authenticated token
	 */
	public Token(VerificationToken vToken) {
		super(null);
		this.vToken = vToken;
		setAuthenticated(false);
	}

	/**
	 * When the user has been verified
	 */
	public Token(VerificationToken vToken, AuthUserDetails principal) {
		super(principal.getAuthorities());
		this.vToken = vToken;
		this.principal = principal;
		setAuthenticated(true);
	}

	public VerificationToken getVToken() {
		return vToken;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}
