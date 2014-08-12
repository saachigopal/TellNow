package com.tellnow.api.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.tellnow.api.domain.VerificationToken;

public class TokenAuthenticationProcessingFilter extends GenericFilterBean {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AuthServiceImpl userService;
	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			Map<String, String[]> params = request.getParameterMap();
			if (!params.isEmpty() && params.containsKey("token")) {
				String token = params.get("token")[0];
				if (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
					VerificationToken vToken = userService.validateAuthenticationToken(token);
					if (vToken != null && !vToken.hasExpired()) {
						SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new Token(vToken)));
					}
				}
			}
		
		} catch (AuthenticationException e) {
			logger.error("AuthenticationException when we try to auto login based on token ", e);
		}
		
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("filterChain.doFilter(request, response);", e);
			logger.info("filterChain.doFilter(request, response) error:", request.toString());
			((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Some error occured, please try to send all informations, or try to re-login");
		}
		
	}
}