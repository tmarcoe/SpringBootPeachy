package com.peachy.reports;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public abstract class PDFReports {
	private float verticalMargin;
	private float horizantleMargin;
	private float lineHeight;
	private float padding;
	
	
	
	public abstract float insertTable(PDPageContentStream content, PDPage page, List<?> items) throws IOException;
	 
	public abstract float insertContent(PDPageContentStream content, PDPage page, List<?> list) throws IOException;
	
	public abstract void insertFooter(PDPageContentStream content, PDPage page, Object...varags) throws IOException;
	
	public abstract void insertHeader(PDPageContentStream content, PDPage page, Object...varargs) throws IOException;
	
	

	
	public void findCenter(String text, PDFont font, float fontSize , PDPage page) throws IOException {
		
		PDRectangle box = page.getMediaBox();
		
		PDFCoordinates.xPos = (box.getWidth() - (font.getStringWidth(text) / 1000 * fontSize))/2;
		PDFCoordinates.yPos = (box.getHeight() - (font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize))/2;
		
	}
	
	public void rightJustify(String text, PDFont font, float fontSize , float right) throws IOException {
		PDFCoordinates.xPos = (right - ((font.getStringWidth(text) / 1000.0f) * fontSize)) - padding;
		
	}
	
	public static class PDFCoordinates {
		public static float xPos;
		public static float yPos;
		
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


	public float getPadding() {
		return padding;
	}


	public void setPadding(float padding) {
		this.padding = padding;
	}

	
}
