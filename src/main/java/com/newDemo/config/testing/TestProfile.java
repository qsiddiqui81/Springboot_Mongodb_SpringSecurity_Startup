package com.newDemo.config.testing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.newDemo.config.EnvConfiguration;

@Configuration
@Profile("test")
public class TestProfile {
	
	@Bean
	public EnvConfiguration getTestConfig(){
		return new TestConfig();
	}
}
