package com.tellnow.api.main;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import com.tellnow.api.config.DatabaseConfig;
import com.tellnow.api.config.SwaggerConfig;
import com.tellnow.api.config.ThumbnailConfig;
import com.tellnow.api.config.WebMvcConfig;
import com.tellnow.api.utils.AutowireHelper;


@ComponentScan({ "com.tellnow.api.controller", "com.tellnow.api.security", "com.tellnow.api.service", "com.tellnow.api.listener", "com.tellnow.api.exceptions.handling" })
@EnableAutoConfiguration
@ImportResource("classpath:security-config.xml")
@Import({ DatabaseConfig.class, WebMvcConfig.class, SwaggerConfig.class, ThumbnailConfig.class })
public class Application {

	@Autowired
	Environment env;

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultiPartConfigFactory factory = new MultiPartConfigFactory();
		factory.setMaxFileSize(env.getProperty("upload.maxFileSize"));
		factory.setMaxRequestSize(env.getProperty("upload.maxRequestSize"));
		return factory.createMultipartConfig();
	}

	@Bean
	AutowireHelper autowireHelper() {
		return AutowireHelper.getInstance();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
