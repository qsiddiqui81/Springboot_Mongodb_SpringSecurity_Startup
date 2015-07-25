package com.newDemo.config.testing;

import org.springframework.beans.factory.annotation.Value;

import com.newDemo.config.EnvConfiguration;

public class TestConfig implements EnvConfiguration{
	
	@Value("${samepinch.serverurl.dev}")
	private String serverUrl;
	@Value("${samepinch.apns.cert.dev}")
	private String apnsCertificate;
	@Value("${samepinch.apns.pass.dev}")
	private String apnsPassword;
	@Value("${samepinch.db.name.test}")
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
