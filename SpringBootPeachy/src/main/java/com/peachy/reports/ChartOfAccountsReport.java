package com.peachy.reports;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.support.PagedListHolder;

import com.peachy.component.ChartOfAccountsReportSettings;
import com.peachy.entity.ChartOfAccounts;



public class ChartOfAccountsReport extends PDFReports {
	private ChartOfAccountsReportSettings ts;
	
	public ChartOfAccountsReport(ChartOfAccountsReportSettings tableSettings) {
		ts = tableSettings;
		setVerticalMargin(ts.getVerticalMargin());
		setHorizantleMargin(ts.getHorizantleMargin());
		setLineHeight(ts.getLineHeight());
		setPadding(ts.getPadding());
		
	}

	public void pdfChartOfAccuntsReport(String fileName, PagedListHolder<ChartOfAccounts> accounts ) throws IOException {
		PDDocument doc = new PDDocument();
		accounts.setPageSize(40);
		for(int i=0; i < accounts.getPageCount(); i++) {
			accounts.setPage(i);
			PDPage page = new PDPage(PDRectangle.LETTER);
			doc.addPage(page);
			PDPageContentStream content = new PDPageContentStream(doc, page);
			insertHeader(content, page);
			insertContent(content, page, accounts.getPageList());
			insertFooter(content, page, i + 1, accounts.getPageCount());
			content.close();
		}
		
		doc.save(fileName);
		doc.close();
	}
	
	
	@Override
	public float insertTable(PDPageContentStream content, PDPage page, List<?> items) throws IOException {
		float lineHeight = getLineHeight();
		float horizantleMargin = getHorizantleMargin();
		float padding = getPadding();
		
		String columnTxt = "";
		float texty;
		float fontSize = ts.getContentSize();
		float columnNameSize = ts.getColumnNameSize();
		float y = (getVerticalMargin() - 100);

		// final float tableWidth = 900.0f;
		final int rows = items.size();
		final float tableHeight = lineHeight * rows;
		final float cellMargin = 1f;

		float textx = horizantleMargin + cellMargin;
		texty = y - 5;
		
		float totalWidth = 0;
		for (int i = 0; i < ts.getColWidths().length; i++) {
			totalWidth += ts.getColWidths()[i];
		}
		totalWidth += horizantleMargin;
		
		for (int i = 0; i < ts.getColNames().length; i++) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_BOLD, columnNameSize);
			content.newLineAtOffset(textx, y + (lineHeight / 2));
			content.showText(ts.getColNames()[i]);
			content.endText();
			textx += ts.getColWidths()[i];

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
		for (int i = 0; i <= ts.getColNames().length; i++) {
			content.moveTo(nextx, y);
			content.lineTo(nextx, y - tableHeight);
			content.stroke();
			if (i < ts.getColNames().length) {
				nextx += ts.getColWidths()[i]; // colWidth;
			}
		}
		texty = y - 8;
		for(Object item: items) {
			textx = horizantleMargin + cellMargin;
			int offset = 0;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((ChartOfAccounts)item).getAccountNum());
			content.endText();
			textx += ts.getColWidths()[offset];
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((ChartOfAccounts)item).getAccountName());
			content.endText();
			textx += ts.getColWidths()[offset];
			offset++;
			float rightSide = textx + ts.getColWidths()[offset];
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			columnTxt = String.format("%.2f", ((ChartOfAccounts)item).getAccountBalance());
			rightJustify(columnTxt, PDType1Font.TIMES_ROMAN, 8, rightSide);
			content.newLineAtOffset(PDFCoordinates.xPos, texty);
			content.showText(columnTxt);
			content.endText();
			textx += ts.getColWidths()[offset];
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			columnTxt = String.valueOf(((ChartOfAccounts)item).isDebitAccount());
			content.showText(columnTxt);
			content.endText();
			textx += ts.getColWidths()[offset];
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((ChartOfAccounts)item).getDescription());
			content.endText();
			textx += ts.getColWidths()[offset];

			texty -= lineHeight;			
		}

		return texty;
	}

	@Override
	public float insertContent(PDPageContentStream content, PDPage page, List<?> list) throws IOException {
		PDFCoordinates.xPos = getHorizantleMargin();
		PDFCoordinates.yPos = 650;
		float nextY = insertTable(content, page, list);
		
		return nextY;
	}

	@Override
	public void insertFooter(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, ts.getFooterSize());
		content.newLineAtOffset(80, getLineHeight() * 2);
		content.showText(String.format("Page %d of %d", varargs[0], varargs[1]));
		content.endText();
	}

	@Override
	public void insertHeader(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		
		findCenter(ts.getHeading(), PDType1Font.TIMES_BOLD_ITALIC, ts.getHeadingSize(), page);
		PDFCoordinates.yPos = 750;
		content.beginText();
		content.newLineAtOffset(PDFCoordinates.xPos, PDFCoordinates.yPos);
		content.setFont(PDType1Font.TIMES_ROMAN, ts.getHeadingSize());
		content.showText(ts.getHeading());
		content.endText();
	}

}
