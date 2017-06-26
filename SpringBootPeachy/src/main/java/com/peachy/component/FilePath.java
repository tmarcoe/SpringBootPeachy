package com.peachy.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "filePath")
@Component
public class FilePath {
	private String imageLoc;
	private String config;
	private String reportPath;
	private String outPath;
	private String imgUploadLoc;
	private String payStubPath;
	
	public String getImageLoc() {
		return imageLoc;
	}

	public void setImageLoc(String imageLoc) {
		this.imageLoc = imageLoc;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getImgUploadLoc() {
		return imgUploadLoc;
	}

	public void setImgUploadLoc(String imgUploadLoc) {
		this.imgUploadLoc = imgUploadLoc;
	}

	public String getPayStubPath() {
		return payStubPath;
	}

	public void setPayStubPath(String payStubPath) {
		this.payStubPath = payStubPath;
	}

}
