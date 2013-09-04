package com.skp.config;

import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.mongodb.Mongo;

@Configuration
public class ComponentConfig {

	@Autowired
	private Environment environment;

	@Bean
	public Mongo mongo() throws NumberFormatException, UnknownHostException {
		Mongo mongo = new Mongo(environment.getProperty("mongodb.host"), new Integer(environment.getProperty("mongodb.port")));
		return mongo;
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new Mongo(environment.getProperty("mongodb.host"), new Integer(environment.getProperty("mongodb.port"))), environment.getProperty("mongodb.dbname"));
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}	

	@Bean
	public JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();	
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("username- to be given");
		mailSender.setPassword("password- to be given");
		Properties javaMailProperties = new Properties();
		//javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", true);
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}


}
