package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "salesReceiptTable")
@Component
public class SalesReceiptSettings  extends PDFTableSettings {


}
