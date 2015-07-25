package com.newDemo.config.production;

import org.springframework.beans.factory.annotation.Value;

import com.newDemo.config.EnvConfiguration;


public class ProductionConfig implements EnvConfiguration {
	
	@Value("${samepinch.serverurl.prod}")
	private String serverUrl;
	@Value("${samepinch.db.name.prod}")
	private String dbName;
	
	@Override
	public String getServerUrl(){
		return serverUrl;
	}

	@Override
	public String getDBName() {
		return dbName;
	}
}
