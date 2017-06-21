package com.peachy.reports;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.support.PagedListHolder;

import com.peachy.component.SalesReceiptSettings;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.helper.AddressLabel;



public class CreatePDFSalesReceipt extends PDFReports {
	private double rate;
	private String currencySymbol;
	private SalesReceiptSettings sRSettings;
	private float leftMargin;

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

	public CreatePDFSalesReceipt(SalesReceiptSettings sr) {
		sRSettings = sr;
		setVerticalMargin(sRSettings.getVerticalMargin());
		setHorizantleMargin(sRSettings.getHorizantleMargin());
		setLineHeight(sRSettings.getLineHeight());
		setPadding(sRSettings.getPadding());

	}

	public void createReceipt(String fileName, double rate, String currencySymbol, Invoice header,
			List<InvoiceItem> items, AddressLabel address) throws IOException {
		PagedListHolder<InvoiceItem> pg = new PagedListHolder<InvoiceItem>(items);
		pg.setPageSize(30);

		setRate(rate);
		setCurrencySymbol(currencySymbol);

		PDDocument doc = new PDDocument();

		for (int i = 0; i < pg.getPageCount(); i++) {
			pg.setPage(i);
			PDPage page = new PDPage();
			doc.addPage(page);
			PDPageContentStream content = new PDPageContentStream(doc, page);
			insertHeader(content, page, header, address);
			float startY = insertContent(content, page, pg.getPageList());
			insertFooter(content, page, header, startY, (i + 1), pg.getPageCount());
			content.close();
		}
		doc.save(fileName);
		doc.close();
		pg = null;
		System.gc();
	}

	public void insertHeader(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		float verticalMargin = getVerticalMargin();
		float lineHeight = getLineHeight();
		Invoice header = (Invoice) varargs[0];
		AddressLabel address = (AddressLabel) varargs[1];
		float currentLine = (verticalMargin - lineHeight);

		content.beginText();
		content.setFont(PDType1Font.TIMES_BOLD_ITALIC, sRSettings.getHeadingSize());
		findCenter("Peachy's Coffee", PDType1Font.TIMES_BOLD_ITALIC, sRSettings.getHeadingSize(), page);
		content.newLineAtOffset(PDFCoordinates.xPos, 750);
		content.showText(sRSettings.getHeading());
		content.endText();

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 10);
		content.newLineAtOffset(20, currentLine);
		content.showText(String.format("Name: %s %s", address.getFirstname(), address.getLastname()));
		content.endText();
		currentLine -= lineHeight;

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 10);
		content.newLineAtOffset(20, currentLine);
		content.showText(String.format("%s", address.getAddress1()));
		content.endText();
		currentLine -= lineHeight;

		if (address.getAddress2().length() > 0) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, 10);
			content.newLineAtOffset(20, currentLine);
			content.showText(String.format("%s", address.getAddress2()));
			content.endText();
			currentLine -= lineHeight;
		}

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 10);
		content.newLineAtOffset(20, currentLine);
		content.showText(String.format("%s, %s, %s", address.getCity(), address.getRegion(), address.getPostalCode()));
		content.endText();
		currentLine -= lineHeight;

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 10);
		content.newLineAtOffset(20, currentLine);
		content.showText(String.format("%s", address.getCountry()));
		content.endText();
		currentLine -= lineHeight;

		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 12);
		content.newLineAtOffset(20, currentLine - 5);
		content.showText(String.format("Invoice #%08d", header.getInvoice_num()));
		content.endText();

	}

	@Override
	public float insertContent(PDPageContentStream content, PDPage page, List<?> list) throws IOException {
		float startY = insertTable(content, page, list);

		return startY;
	}

	public void insertFooter(PDPageContentStream content, PDPage page, Object... varargs) throws IOException {
		float lineHeight = getLineHeight();
		Invoice header = (Invoice) varargs[0];
		float startY = (float) varargs[1];
		float fontSize = sRSettings.getContentSize();
		float footerMargin = 370;
		int curPg = (int) varargs[2];
		int totalPg = (int) varargs[3];
		if (curPg >= totalPg) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(footerMargin, startY);
			content.showText("SubTotal");
			content.endText();

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			String subTotal = String.format("%s%5.2f", getCurrencySymbol(), header.getTotal() * getRate());
			rightJustify(subTotal, PDType1Font.TIMES_ROMAN, fontSize, leftMargin);
			content.newLineAtOffset(PDFCoordinates.xPos, startY);
			content.showText(subTotal);
			content.endText();

			startY -= lineHeight;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(footerMargin, startY);
			content.showText("Total Tax");
			content.endText();

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			String totalTax = String.format("%s%5.2f", getCurrencySymbol(), header.getTotal_tax() * getRate());
			rightJustify(totalTax, PDType1Font.TIMES_ROMAN, fontSize, leftMargin);
			content.newLineAtOffset(PDFCoordinates.xPos, startY);
			content.showText(totalTax);
			content.endText();

			startY -= lineHeight;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(footerMargin, startY);
			content.showText("Added Charges");
			content.endText();

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			String addedCharges = String.format("%s%5.2f", getCurrencySymbol(), header.getAdded_charges() * getRate());
			rightJustify(addedCharges, PDType1Font.TIMES_ROMAN, fontSize, leftMargin);
			content.newLineAtOffset(PDFCoordinates.xPos, startY);
			content.showText(addedCharges);
			content.endText();

			startY -= lineHeight;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(footerMargin, startY);
			content.showText("Shipping Charges");
			content.endText();

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			String shippingCharges = String.format("%s%5.2f", getCurrencySymbol(),
					header.getShipping_cost() * getRate());
			rightJustify(shippingCharges, PDType1Font.TIMES_ROMAN, fontSize, leftMargin);
			content.newLineAtOffset(PDFCoordinates.xPos, startY);
			content.showText(shippingCharges);
			content.endText();

			startY -= lineHeight;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(footerMargin, startY);
			content.showText("Grand Total");
			content.endText();

			double grandTotal = (header.getTotal() + header.getTotal_tax() + header.getAdded_charges()
					+ header.getShipping_cost()) * getRate();
			String strGrandTotal = String.format("%s%5.2f", getCurrencySymbol(), grandTotal);
			rightJustify(strGrandTotal, PDType1Font.TIMES_ROMAN, fontSize, leftMargin);
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(PDFCoordinates.xPos, startY);
			content.showText(strGrandTotal);
			content.endText();
		}
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
		content.newLineAtOffset(80, lineHeight);
		String pgStr = String.format("Page %d of %d      ", curPg, totalPg);
		content.showText(pgStr + sRSettings.getFooting());
		content.endText();
	}

	@Override
	public float insertTable(PDPageContentStream content, PDPage page, List<?> items) throws IOException {
		float lineHeight = getLineHeight();
		float horizantleMargin = getHorizantleMargin();
		float padding = getPadding();

		float texty;
		float fontSize = sRSettings.getContentSize();
		float columnNameSize = sRSettings.getColumnNameSize();
		float y = (getVerticalMargin() - 100);

		// final float tableWidth = 900.0f;
		final int rows = items.size();
		final float tableHeight = lineHeight * rows;
		final float cellMargin = 1f;

		float textx = horizantleMargin + cellMargin;
		texty = y - 5;

		float totalWidth = 0;
		for (int i = 0; i < sRSettings.getColWidths().length; i++) {
			totalWidth += sRSettings.getColWidths()[i];
		}
		totalWidth += horizantleMargin;
		leftMargin = totalWidth;
		for (int i = 0; i < sRSettings.getColNames().length; i++) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_BOLD, columnNameSize);
			content.newLineAtOffset(textx, y + (lineHeight / 2));
			content.showText(sRSettings.getColNames()[i]);
			content.endText();
			textx += sRSettings.getColWidths()[i];

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
		for (int i = 0; i <= sRSettings.getColNames().length; i++) {
			content.moveTo(nextx, y);
			content.lineTo(nextx, y - tableHeight);
			content.stroke();
			if (i < sRSettings.getColNames().length) {
				nextx += sRSettings.getColWidths()[i]; // colWidth;
			}
		}
		// now add the text
		// textx = margin+cellMargin;
		texty = y - 8;
		for (Object item : items) {
			textx = horizantleMargin + cellMargin;

			int offset = 0;
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(String.valueOf(((InvoiceItem) item).getAmount()));
			content.endText();
			textx += sRSettings.getColWidths()[offset];
			offset++;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((InvoiceItem) item).getSku_num());
			content.endText();
			textx += sRSettings.getColWidths()[offset];
			offset++;

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((InvoiceItem) item).getProduct_name());
			content.endText();
			textx += sRSettings.getColWidths()[offset];
			offset++;
			
			if (((InvoiceItem) item).getOptions() ==  null) {
				((InvoiceItem) item).setOptions("");
			}
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			content.newLineAtOffset(textx + padding, texty);
			content.showText(((InvoiceItem) item).getOptions());
			content.endText();
			textx += sRSettings.getColWidths()[offset];
			offset++;
			
			float rightSide = textx + sRSettings.getColWidths()[offset];

			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);

			String price = String.format("%s%5.2f", getCurrencySymbol(),
					(((InvoiceItem) item).getPrice() * ((InvoiceItem) item).getAmount()) * getRate());

			rightJustify(price, PDType1Font.TIMES_ROMAN, fontSize, rightSide);
			content.newLineAtOffset(PDFCoordinates.xPos, texty);
			content.showText(price);
			content.endText();
			textx += sRSettings.getColWidths()[offset];
			offset++;
			rightSide = textx + sRSettings.getColWidths()[offset];
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
			String tax = String.format("%s%5.2f", getCurrencySymbol(),
					(((InvoiceItem) item).getTax() * ((InvoiceItem) item).getAmount()) * getRate());
			rightJustify(tax, PDType1Font.TIMES_ROMAN, fontSize, rightSide);
			content.newLineAtOffset(PDFCoordinates.xPos, texty);
			content.showText(tax);
			content.endText();

			texty -= lineHeight; // row height

		}

		return texty;
	}

}
