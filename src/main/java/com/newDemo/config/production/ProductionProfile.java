package com.newDemo.config.production;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.newDemo.config.EnvConfiguration;

@Configuration
@Profile("prod")
public class ProductionProfile {
	
	@Bean
	public EnvConfiguration getProductionConfig(){
		return new ProductionConfig();
	}
}
