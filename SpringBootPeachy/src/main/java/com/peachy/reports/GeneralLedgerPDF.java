package com.peachy.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.support.PagedListHolder;

import com.peachy.component.GeneralLedgerReportSettings;
import com.peachy.entity.GeneralLedger;


public class GeneralLedgerPDF extends PDFReports {
	private GeneralLedgerReportSettings gLSettings;
	public GeneralLedgerPDF(GeneralLedgerReportSettings gLSettings) {
		setVerticalMargin(gLSettings.getVerticalMargin());
		setHorizantleMargin(gLSettings.getHorizantleMargin());
		setLineHeight(gLSettings.getLineHeight());
		setPadding(gLSettings.getPadding());
	}
	
	public void pdfLedgerReport(String fileName, PagedListHolder<GeneralLedger> ledger, Date start, Date end) throws IOException {
		PDDocument doc = new PDDocument();

		
		ledger.setPageSize(40);

		for (int i = 0; i < ledger.getPageCount(); i++) {
			ledger.setPage(i);
			PDPage page = new PDPage(PDRectangle.LETTER);
			doc.addPage(page);
			PDPageContentStream content = new PDPageContentStream(doc, page);
			insertHeader(content, page, start, end);
			insertContent(content, page, ledger.getPageList());
			insertFooter(content, page, i + 1, ledger.getPageCount());
			content.close();
		}
		doc.save(fileName);
		doc.close();

	}

	public void insertHeader(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		findCenter(gLSettings.getHeading(), PDType1Font.TIMES_BOLD_ITALIC, gLSettings.getHeadingSize(), page);
		PDFCoordinates.yPos = 750;
		content.beginText();
		content.newLineAtOffset(PDFCoordinates.xPos, PDFCoordinates.yPos);
		content.setFont(PDType1Font.TIMES_ROMAN, gLSettings.getHeadingSize());
		content.showText(gLSettings.getHeading());
		content.endText();
		
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
		String stDate = df.format(varargs[0]);
		String endDate  = df.format(varargs[1]);
		String heading = String.format("Starting: %s - Ending: %s", stDate, endDate);
		findCenter(heading, PDType1Font.TIMES_BOLD_ITALIC, 14, page);
		PDFCoordinates.yPos = 730;
		
		content.beginText();
		content.newLineAtOffset(PDFCoordinates.xPos, PDFCoordinates.yPos);
		content.setFont(PDType1Font.TIMES_ROMAN, 14);
		content.showText(heading);
		content.endText();
	
		
	}
	
	
	public float insertContent(PDPageContentStream content, PDPage page, List<?> list) throws IOException {
		PDFCoordinates.xPos = gLSettings.getHorizantleMargin();
		PDFCoordinates.yPos = 650;
		float nextY = insertTable(content, page, list);
		
		return nextY;
	}
	
	public void insertFooter(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		float lineHeight = getLineHeight();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, gLSettings.getFooterSize());
		content.newLineAtOffset(80, lineHeight * 2);
		content.showText(String.format("Page %d of %d", varargs[0], varargs[1]));
		content.endText();
	}
	
	public float insertTable(PDPageContentStream content, PDPage page, List<?> items) throws IOException{
		float lineHeight = getLineHeight();
		float horizantleMargin = getHorizantleMargin();
		float padding = getPadding();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String columnTxt = "";
		float texty;
		float fontSize = gLSettings.getContentSize();
		float columnNameSize = gLSettings.getColumnNameSize();
		float y = (getVerticalMargin() - 100);

		// final float tableWidth = 900.0f;
		final int rows = items.size();
		final float tableHeight = lineHeight * rows;
		final float cellMargin = 1f;

		float textx = horizantleMargin + cellMargin;
		texty = y - 5;
		
		float totalWidth = 0;
		for (int i = 0; i < gLSettings.getColWidths().length; i++) {
			totalWidth += gLSettings.getColWidths()[i];
		}
		totalWidth += horizantleMargin;
		
		for (int i = 0; i < gLSettings.getColNames().length; i++) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_BOLD, columnNameSize);
			content.newLineAtOffset(textx, y + (lineHeight / 2));
			content.showText(gLSettings.getColNames()[i]);
			content.endText();
			textx += gLSettings.getColWidths()[i];

		}
		// draw the rows
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			content.moveTo(horizantleMargin, nexty);
			content.lineTo(totalWidth, nexty);
			content.stroke();
			nexty -= lineHeight;
		}
		
		// draw the columns
		float nextx = horizantleMargin;
		for (int i = 0; i <= gLSettings.getColNames().length; i++) {
			content.moveTo(nextx, y);
			content.lineTo(nextx, y - tableHeight);
			content.stroke();
			if (i < gLSettings.getColNames().length) {
				nextx += gLSettings.getColWidths()[i]; // colWidth;
			}
		}

		// now add the text
		// textx = margin+cellMargin;
		texty = y - 8;
		
		for (Object item : items) {
			if (((GeneralLedger) item).getDebitAmt() == 0 && ((GeneralLedger) item).getCreditAmt() == 0) continue;
			textx = horizantleMargin + cellMargin;
			int offset = 0;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(df.format(((GeneralLedger) item).getEntryDate()));
			content.endText();
			textx += gLSettings.getColWidths()[offset];
			offset++;
			
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((GeneralLedger) item).getAccountNum());
			content.endText();
			textx += gLSettings.getColWidths()[offset];
			offset++;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((GeneralLedger) item).getDescription());
			content.endText();
			textx += gLSettings.getColWidths()[offset];
			offset++;			
			textx += gLSettings.getColWidths()[offset];			
			offset++;


			if (((GeneralLedger) item).getDebitAmt() != 0) {
				content.beginText();
				content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
				columnTxt = String.format("%.2f", ((GeneralLedger) item).getDebitAmt());
				rightJustify(columnTxt, PDType1Font.TIMES_ROMAN, fontSize, textx);
				content.newLineAtOffset(PDFCoordinates.xPos, texty);
				content.showText(columnTxt);
				content.endText();
			}
			
			textx = totalWidth;
			if (((GeneralLedger) item).getCreditAmt() != 0) {
				content.beginText();
				content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
				columnTxt = String.format("%.2f", ((GeneralLedger) item).getCreditAmt());
				rightJustify(columnTxt, PDType1Font.TIMES_ROMAN, fontSize, textx);
				content.newLineAtOffset(PDFCoordinates.xPos, texty);
				content.showText(columnTxt);
				content.endText();
			}
			texty -= lineHeight; // row height
		}

		
		return texty;
	}

}
