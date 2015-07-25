package com.newDemo.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class ResourceProperties {

	/**
	 * This method return properties on the basis of config file path
	 * @param configFilePath
	 * @return
	 * @throws IOException
	 */
	public Properties getResourceProperties(String configFilePath) throws IOException{
		Properties configProp = new Properties();
		InputStream in = this.getClass().getResourceAsStream(configFilePath);
        configProp.load(in);
		return configProp;
	}
}
