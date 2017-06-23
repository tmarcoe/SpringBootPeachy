package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "payStubTableSettings")
@Component
public class PayStubReportSettings extends PDFTableSettings {

}
