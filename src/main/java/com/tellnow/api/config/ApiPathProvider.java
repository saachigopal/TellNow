package com.tellnow.api.config;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import com.mangofactory.swagger.core.SwaggerPathProvider;

public class ApiPathProvider implements SwaggerPathProvider {

	private SwaggerPathProvider defaultSwaggerPathProvider;
	
	@Autowired
	private ServletContext servletContext;

	@Override
	public String getApiResourcePrefix() {
		return defaultSwaggerPathProvider.getApiResourcePrefix();
	}

	public String getAppBasePath() {
		return UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:8080").path(servletContext.getContextPath()).build().toString();
	}

	@Override
	public String sanitizeRequestMappingPattern(String requestMappingPattern) {
		return defaultSwaggerPathProvider.sanitizeRequestMappingPattern(requestMappingPattern);
	}

	public void setDefaultSwaggerPathProvider(SwaggerPathProvider defaultSwaggerPathProvider) {
		this.defaultSwaggerPathProvider = defaultSwaggerPathProvider;
	}
}