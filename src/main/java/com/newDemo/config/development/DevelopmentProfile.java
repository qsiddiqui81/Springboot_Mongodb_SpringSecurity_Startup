package com.newDemo.config.development;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.newDemo.config.EnvConfiguration;

@Configuration
@Profile("dev")
public class DevelopmentProfile {
	
	@Bean
	public EnvConfiguration getDevelopmentConfig(){
		return new DevelopmentConfig();
	}
}
