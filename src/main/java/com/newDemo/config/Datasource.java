package com.newDemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class Datasource extends AbstractMongoConfiguration{
	
	@Autowired EnvConfiguration envConfiguration;
	
	@Override
	public String getDatabaseName() {
		return envConfiguration.getDBName();
	}
 
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("127.0.0.1");
	}
}