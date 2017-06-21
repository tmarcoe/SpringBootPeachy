package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "generalLedgerTable")
@Component
public class GeneralLedgerReportSettings extends PDFTableSettings {
	

}
