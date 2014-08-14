package com.tellnow.api.main;

import javax.servlet.MultipartConfigElement;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.tellnow.api.config.DatabaseConfig;
import com.tellnow.api.config.SwaggerConfig;
import com.tellnow.api.config.ThumbnailConfig;
import com.tellnow.api.config.WebMvcConfig;
import com.tellnow.api.service.CleanerService;
import com.tellnow.api.utils.AutowireHelper;

@ComponentScan({ "com.tellnow.api.controller", "com.tellnow.api.security", "com.tellnow.api.service", "com.tellnow.api.listener", "com.tellnow.api.exceptions.handling" })
@EnableAutoConfiguration
@ImportResource("classpath:security-config.xml")
@Import({ DatabaseConfig.class, WebMvcConfig.class, SwaggerConfig.class, ThumbnailConfig.class })
public class Application {

	private static final String DEFAULT_CRON_EXPRESSION_VALUE = "0 * * * * ?";
	private static final String NAME_CRON_EXPRESSION_ENV = "cleaner.cron.expression";
	private String cronExp = "";
	
	@Autowired
	Environment env;
	
	@Autowired
	CleanerService cleanerService;
	
	@Autowired
	MethodInvokingJobDetailFactoryBean methodInvokingJobDetail;
	
	@Autowired 
	CronTriggerFactoryBean cronTriggerFactoryBean;
	
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

	// Beans for cron job scheduler
	@Bean
	MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean() {
		MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean =
			new MethodInvokingJobDetailFactoryBean();
		methodInvokingJobDetailFactoryBean.setTargetObject(cleanerService);
		methodInvokingJobDetailFactoryBean.setTargetMethod("clean");
		methodInvokingJobDetailFactoryBean.setArguments(new String[]{cronExp});
		return methodInvokingJobDetailFactoryBean;
	}
	
	@Bean 
	CronTriggerFactoryBean cronTriggerBean() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(methodInvokingJobDetail.getObject());
		String cronExpressionStringValue = env.getProperty(NAME_CRON_EXPRESSION_ENV);
		if (cronExpressionStringValue != null) {
			cronExp = cronExpressionStringValue;
		} else {
			cronExp = DEFAULT_CRON_EXPRESSION_VALUE;
		}
		cronTriggerFactoryBean.setCronExpression(cronExp);
		methodInvokingJobDetail.setArguments(new String[]{cronExp});
		return cronTriggerFactoryBean;
	}
	
	@Bean
	SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		Trigger[] triggers = new Trigger[]{cronTriggerFactoryBean.getObject()};
		schedulerFactoryBean.setTriggers(triggers);
		return schedulerFactoryBean;
	}
	// End of beans for cron job scheduler
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
