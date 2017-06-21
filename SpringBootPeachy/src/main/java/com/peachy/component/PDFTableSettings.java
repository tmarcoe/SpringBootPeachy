package com.peachy.component;

public class PDFTableSettings {
	
	private String heading;
	private String footing;
	private float verticalMargin;
	private float horizantleMargin;
	private float lineHeight;
	private float columnNameSize;
	private float contentSize;
	private float headingSize;
	private float footerSize;
	private float padding;
	private float[] colWidths;
	private String[] colNames; 
	
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getFooting() {
		return footing;
	}
	public void setFooting(String footing) {
		this.footing = footing;
	}
	public float getVerticalMargin() {
		return verticalMargin;
	}
	public void setVerticalMargin(float verticalMargin) {
		this.verticalMargin = verticalMargin;
	}
	public float getHorizantleMargin() {
		return horizantleMargin;
	}
	public void setHorizantleMargin(float horizantleMargin) {
		this.horizantleMargin = horizantleMargin;
	}
	public float getLineHeight() {
		return lineHeight;
	}
	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}
	public float getColumnNameSize() {
		return columnNameSize;
	}
	public void setColumnNameSize(float columnNameSize) {
		this.columnNameSize = columnNameSize;
	}
	public float getContentSize() {
		return contentSize;
	}
	public void setContentSize(float contentSize) {
		this.contentSize = contentSize;
	}
	public float getHeadingSize() {
		return headingSize;
	}
	public void setHeadingSize(float headingSize) {
		this.headingSize = headingSize;
	}
	public float getFooterSize() {
		return footerSize;
	}
	public void setFooterSize(float footerSize) {
		this.footerSize = footerSize;
	}
	public float[] getColWidths() {
		return colWidths;
	}
	public void setColWidths(float[] colWidths) {
		this.colWidths = colWidths;
	}
	public String[] getColNames() {
		return colNames;
	}
	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}
	public float getPadding() {
		return padding;
	}
	public void setPadding(float padding) {
		this.padding = padding;
	}
	
}
