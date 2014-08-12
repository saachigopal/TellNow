package com.tellnow.api.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.googlecode.flyway.core.Flyway;
import com.jolbox.bonecp.BoneCPDataSource;

//@EnableTransactionManagement(proxyTargetClass = false)
@EnableJpaRepositories("com.tellnow.api.repository")
@Configuration
public class DatabaseConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	Environment env;

	@Bean
	public BoneCPDataSource boneCPDataSource() {

		BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
		boneCPDataSource.setDriverClass("com.mysql.jdbc.Driver");
		boneCPDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		boneCPDataSource.setUsername(env.getProperty("jdbc.username"));
		boneCPDataSource.setPassword(env.getProperty("jdbc.password"));
		boneCPDataSource.setIdleConnectionTestPeriodInMinutes(Long.parseLong(env.getProperty("bonecp.idleConnectionTestPeriodInMinutes")));
		boneCPDataSource.setIdleMaxAgeInMinutes(Long.parseLong(env.getProperty("bonecp.idleMaxAgeInMinutes")));
		boneCPDataSource.setMaxConnectionsPerPartition(Integer.parseInt(env.getProperty("bonecp.maxConnectionsPerPartition")));
		boneCPDataSource.setMinConnectionsPerPartition(Integer.parseInt(env.getProperty("bonecp.minConnectionsPerPartition")));
		boneCPDataSource.setPartitionCount(Integer.parseInt(env.getProperty("bonecp.partitionCount")));
		boneCPDataSource.setAcquireIncrement(Integer.parseInt(env.getProperty("bonecp.acquireIncrement")));
		boneCPDataSource.setStatementsCacheSize(Integer.parseInt(env.getProperty("bonecp.statementsCacheSize")));

		return boneCPDataSource;

	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	@Autowired
	public EntityManagerFactory entityManagerFactory(BoneCPDataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(false);
		vendorAdapter.setDatabase(Database.MYSQL);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(new String[] { "com.tellnow.api.domain" });//add additional packages here
		factory.setDataSource(dataSource);

		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		properties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
		properties.setProperty("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
		properties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
		properties.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
		properties.setProperty("hibernate.search.default.directory_provider", env.getProperty("hibernate.search.default.directory_provider"));
		properties.setProperty("hibernate.search.default.indexBase", env.getProperty("hibernate.search.default.indexBase"));

		factory.setJpaProperties(properties);

		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		JpaDialect jpaDialect = new HibernateJpaDialect();
		txManager.setEntityManagerFactory(entityManagerFactory);
		txManager.setJpaDialect(jpaDialect);
		return txManager;
	}

	@Bean
	public Flyway flyway() {
		final Flyway flyway = new Flyway();
		flyway.setInitOnMigrate(true);
		flyway.setDataSource(boneCPDataSource());
		String locationsValue = env.getProperty("flyway.migrations.location");
		String[] locations = locationsValue.split("\\s*,\\s*");
		flyway.setLocations(locations);
		flyway.migrate();
		return flyway;
	}

}