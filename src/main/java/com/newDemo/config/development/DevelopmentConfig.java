package com.newDemo.config.development;

import org.springframework.beans.factory.annotation.Value;

import com.newDemo.config.EnvConfiguration;

public class DevelopmentConfig implements EnvConfiguration{
	
	@Value("${samepinch.serverurl.dev}")
	private String serverUrl;
	@Value("${samepinch.db.name.dev}")
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
