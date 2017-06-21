package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "fetal")
@Component
public class FetalConfigurator {
	
	private String properiesFile;

	public String getProperiesFile() {
		return properiesFile;
	}

	public void setProperiesFile(String properiesFile) {
		this.properiesFile = properiesFile;
	}

}
