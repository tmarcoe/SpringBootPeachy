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

import com.peachy.component.PayStubReportSettings;
import com.peachy.entity.Employee;
import com.peachy.entity.PaymentRegister;
import com.peachy.entity.TimeSheet;


public class CreatePayStub extends PDFReports{
	private double rate;
	private String currencySymbol;

	private PayStubReportSettings payStub;
	


	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	
	public CreatePayStub(PayStubReportSettings payStub) {
		this.payStub = payStub;
		setVerticalMargin(payStub.getVerticalMargin());
		setHorizantleMargin(payStub.getHorizantleMargin());
		setLineHeight(payStub.getLineHeight());
		setPadding(payStub.getPadding());
	}

	
	public void pdfPayStub(String fileName, List<TimeSheet> items, Employee employee, PaymentRegister pr) throws IOException {
		PDDocument doc = new PDDocument();
		PDPage page = new PDPage(PDRectangle.LETTER);
		doc.addPage(page);
		Date startPeiod = pr.getStartPeriod();


		PDPageContentStream content = new PDPageContentStream(doc, page);
		insertHeader(content, page, startPeiod);
		float nextY = insertContent(content, page, items);
		insertFooter(content, page, pr, employee, nextY);

		content.close();
		doc.save(fileName);
		doc.close();
		
	}
	
	@Override
	public void insertHeader(PDPageContentStream content, PDPage page, Object...vararg) throws IOException {

		findCenter(payStub.getHeading(), PDType1Font.TIMES_BOLD_ITALIC, payStub.getHeadingSize(), page);
		PDFCoordinates.yPos = 750;
		content.beginText();
		content.newLineAtOffset(PDFCoordinates.xPos, PDFCoordinates.yPos);
		content.setFont(PDType1Font.TIMES_ROMAN, payStub.getHeadingSize());
		content.showText(payStub.getHeading());
		content.endText();
		
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
		String heading = df.format((Date) vararg[0]);
		
		findCenter("Starting Period: " + heading, PDType1Font.TIMES_BOLD_ITALIC, 14, page);
		PDFCoordinates.yPos = 730;
		
		content.beginText();
		content.newLineAtOffset(PDFCoordinates.xPos, PDFCoordinates.yPos);
		content.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
		content.showText("Starting Period: " + heading);
		content.endText();
	}
	
	public float insertContent(PDPageContentStream content, PDPage page, List<?> items) throws IOException {
		PDFCoordinates.xPos = payStub.getHorizantleMargin();
		PDFCoordinates.yPos = 650;
		float nextY = insertTable(content, page, items);
		
		return nextY;
	}
	
	@Override
	public void insertFooter(PDPageContentStream content, PDPage page, Object...varargs) throws IOException {
		Employee employee = (Employee) varargs[1];
		PaymentRegister pr = (PaymentRegister) varargs[0];
		float startY = (float) varargs[2];
		float fontSize = payStub.getContentSize();
		final float colOffset = 10;
		float[] column = {getHorizantleMargin() + 1, getHorizantleMargin() + 161, getHorizantleMargin() + 241, getHorizantleMargin() + 411};
		startY -= getLineHeight();
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("Gross Wages");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		String subTotal = String.format("P%.2f", pr.getGrossWage());
		float fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		float rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getWagesYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		

		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("Federal Withholding");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getFederalTx());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getfTaxYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("State Withholding");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getStateTx());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		 rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getsTaxYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		 rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
	
		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("Federal Unemployment");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getFederalUnEm());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getfUnYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("State Unemployment");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getStateUnEm());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getsUnYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("Medicare Tax");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getMedical());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getMedYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("SSI");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", pr.getSsiTx());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(column[2], startY);
		content.showText("Year to Date");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		subTotal = String.format("P%.2f", employee.getSsiYtd());
		fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[3] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();
		
		if (pr.getRetirement() > 0) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[0], startY);
			content.showText("Retirement");
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", pr.getRetirement());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[1] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[2], startY);
			content.showText("Year to Date");
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", employee.getRetireYtd());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[3] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			startY -= getLineHeight();
		}
		if (pr.getGarnishment() > 0) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[0], startY);
			content.showText("Garnishment");
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", pr.getGarnishment());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[1] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[2], startY);
			content.showText("Year to Date");
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", employee.getGarnishmentYtd());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[3] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			startY -= getLineHeight();
		}
		if (pr.getOther() > 0) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[0], startY);
			content.showText(String.format("Other (%s)", pr.getOtherExpl()));
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", pr.getOther());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[1] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(column[2], startY);
			content.showText(String.format("Year to Date", pr.getOtherExpl()));
			content.endText();
		
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			subTotal = String.format("P%.2f", employee.getOtherYtd());
			fieldLength = PDType1Font.TIMES_ROMAN.getStringWidth(subTotal) * (fontSize /1000);	
			rJustify = column[3] - (fieldLength + colOffset);
			content.newLineAtOffset(rJustify, startY);
			content.showText(subTotal);
			content.endText();
		
			startY -= getLineHeight();
		}
		startY -= getLineHeight();
		content.beginText();
		content.setFont(PDType1Font.TIMES_BOLD, fontSize);
		content.newLineAtOffset(column[0], startY);
		content.showText("Net Wages");
		content.endText();
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_BOLD, fontSize);
		subTotal = String.format("P%.2f", pr.getNetWage());
		fieldLength = PDType1Font.TIMES_BOLD.getStringWidth(subTotal) * (fontSize /1000);	
		rJustify = column[1] - (fieldLength + colOffset);
		content.newLineAtOffset(rJustify, startY);
		content.showText(subTotal);
		content.endText();
		
		startY -= getLineHeight();


}
	public float insertTable(PDPageContentStream content, PDPage page, List<?> items) throws IOException{
		float texty;
		float fontSize = payStub.getContentSize();
		float columnNameSize = payStub.getColumnNameSize();
		float y = (payStub.getVerticalMargin() - 100);

		// final float tableWidth = 900.0f;
		final int rows = items.size();
		final float tableHeight = payStub.getLineHeight() * rows;
		final float cellMargin = 1f;

		float textx = payStub.getHorizantleMargin() + getPadding();
		texty = y - 5;
		
		float totalWidth = 0;
		for (int i = 0; i < payStub.getColWidths().length; i++) {
			totalWidth += payStub.getColWidths()[i];
		}
		totalWidth += payStub.getHorizantleMargin();
		
		for (int i = 0; i < payStub.getColNames().length; i++) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_BOLD, columnNameSize);
			content.newLineAtOffset(textx, y + (getLineHeight() / 2));
			content.showText(payStub.getColNames()[i]);
			content.endText();
			textx += payStub.getColWidths()[i];

		}
		// draw the rows
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			content.moveTo(payStub.getHorizantleMargin(), nexty);
			content.lineTo(totalWidth, nexty);
			content.stroke();
			nexty -= getLineHeight();
		}
		
		// draw the columns
		float nextx = payStub.getHorizantleMargin();
		for (int i = 0; i <= payStub.getColNames().length; i++) {
			content.moveTo(nextx, y);
			content.lineTo(nextx, y - tableHeight);
			content.stroke();
			if (i < payStub.getColNames().length) {
				nextx += payStub.getColWidths()[i]; // colWidth;
			}
		}

		// now add the text
		// textx = margin+cellMargin;
		texty = y - 8;
		float totalSu = (float) 0.0;
		float totalMo = (float) 0.0;
		float totalTu = (float) 0.0;
		float totalWe = (float) 0.0;
		float totalTh = (float) 0.0;
		float totalFr = (float) 0.0;
		float totalSa = (float) 0.0;
		
		for (Object item : items) {
			textx = getHorizantleMargin() + cellMargin;
			float accountTotal = (float) 0.0;
			int offset = 0;
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.valueOf(((TimeSheet) item).getAccountNum()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			offset++;
			
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getSunday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getSunday();
			totalSu += ((TimeSheet) item).getSunday();
			offset++;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f",((TimeSheet) item).getMonday()));
			content.endText();
			textx += payStub.getColWidths()[offset];			
			accountTotal += ((TimeSheet)item).getMonday();
			totalMo += ((TimeSheet) item).getMonday();
			offset++;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getTuesday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getTuesday();
			totalTu += ((TimeSheet) item).getTuesday();
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getWednesday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getWednesday();
			totalWe += ((TimeSheet) item).getWednesday();
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getThursday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getThursday();
			totalTh += ((TimeSheet) item).getThursday();
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getFriday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getFriday();
			totalFr += ((TimeSheet) item).getFriday();
			offset++;
			
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", ((TimeSheet) item).getSaturday()));
			content.endText();
			textx += payStub.getColWidths()[offset];
			accountTotal += ((TimeSheet) item).getSaturday();
			totalSa += ((TimeSheet) item).getSaturday();

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + getPadding(), texty);
			content.showText(String.format("%.2f", accountTotal));
			content.endText();
	
			texty -= payStub.getLineHeight(); // row height
		}

		int offset = 0;
		textx = payStub.getHorizantleMargin();
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText("Totals ->");
		content.endText();
		textx += payStub.getColWidths()[offset];
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalSu));
		content.endText();
		textx += payStub.getColWidths()[offset];
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalMo));
		content.endText();
		textx += payStub.getColWidths()[offset];			
		offset++;

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalTu));
		content.endText();
		textx += payStub.getColWidths()[offset];
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalWe));
		content.endText();
		textx += payStub.getColWidths()[offset];
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalTh));
		content.endText();
		textx += payStub.getColWidths()[offset];		
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalFr));
		content.endText();
		textx += payStub.getColWidths()[offset];
		offset++;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", totalSa));
		content.endText();
		textx += payStub.getColWidths()[offset];

		float grandTotal = totalSu + totalMo + totalTu + totalWe + totalTh + totalFr + totalSa;
		
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(textx + getPadding(), texty);
		content.showText(String.format("%.2f", grandTotal));
		content.endText();
		
		texty -= getLineHeight(); // row height	
		
		return texty;
	}


}
