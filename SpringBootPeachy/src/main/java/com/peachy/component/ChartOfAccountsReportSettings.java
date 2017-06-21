package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "chartOfAccountsTable")
@Component
public class ChartOfAccountsReportSettings extends PDFTableSettings {

}
