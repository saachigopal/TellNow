package com.tellnow.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * This entry point is just what Spring should do when it notices a user tries to do an action which requires permissions, but is not authenticated (logged in). The default is to redirect to a web
 * page which asks for username&password and also some other stuff not needed, so we override and just send a normal http status message.
 */
public class TokenEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
