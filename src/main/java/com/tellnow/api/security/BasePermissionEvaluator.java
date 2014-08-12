package com.tellnow.api.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class BasePermissionEvaluator implements PermissionEvaluator {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		logger.info("BasePermissionEvaluator.hasPermission()" + targetDomainObject + " - " + permission);
		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		// TODO Auto-generated method stub
		return true;
	}
}