package com.tellnow.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Will try to authenticate a user based on a token, and if the token is valid it will create a new token holding the proper authorities and authUserDetails
 */
@Service
public class TokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AuthServiceImpl authService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Token token = (Token) authentication;
		if (authService.isValid(token.getVToken())) {
			AuthUserDetails authUserDetails = authService.getAuthoritiesForUser(token.getVToken().getProfileID());
			// Return an updated token with the right user details
			return new Token(token.getVToken(), authUserDetails);
		}
		throw new BadCredentialsException("Invalid token");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(Token.class);
	}

}
