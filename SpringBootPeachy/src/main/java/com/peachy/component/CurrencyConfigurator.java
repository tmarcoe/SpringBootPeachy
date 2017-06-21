package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "currency")
@Component
public class CurrencyConfigurator {
	private String currencyUrl;
	private String baseCurrency;
	private String currencyFile;
	
	public String getCurrencyUrl() {
		return currencyUrl;
	}
	public void setCurrencyUrl(String currencyUrl) {
		this.currencyUrl = currencyUrl;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getCurrencyFile() {
		return currencyFile;
	}
	public void setCurrencyFile(String currencyFile) {
		this.currencyFile = currencyFile;
	}
}

