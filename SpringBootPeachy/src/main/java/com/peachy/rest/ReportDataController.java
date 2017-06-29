package com.peachy.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peachy.component.FilePath;
import com.peachy.json.JSONReports;
import com.peachy.service.InvoiceService;
import com.peachy.service.PurchaseOrderService;
import com.peachy.service.SurveyService;
import com.peachy.service.UserProfileService;


@RestController
public class ReportDataController {

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	SurveyService surveyService;
	
	@Autowired
	PurchaseOrderService ps;

	@Autowired
	FilePath fl;

	@RequestMapping("/reports/sales")
	public String getSalesData(@ModelAttribute("year") String year) throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(df.parse(year));
		String heading = "Sales totals by month for " + c.get(Calendar.YEAR) + " (in pesos)";
		JSONReports report = new JSONReports();
		List<Double> totals = invoiceService.getSalesData(c.get(Calendar.YEAR));

		return report.JSONCreateSalesReport(totals, heading).toString();
	}

	@RequestMapping("/reports/genders")
	public String getGenderData() throws IOException {
		List<Double> genders = userProfileService.getGenderReport();
		String heading = "Customer Breakdown by Gender";
		JSONReports report = new JSONReports();

		return report.JSONCreateGenderReport(genders, heading).toString();
	}

	@RequestMapping("/reports/surveydata")
	public String getSurveyData() throws IOException {
		List<Double> surveys = surveyService.getSurveyReport();
		String heading = "Sactisfaction Survey Report";
		JSONReports report = new JSONReports();

		return report.JSONCreateServeyReport(surveys, heading).toString();
	}

	@RequestMapping("/reports/customercount")
	public String getCustomerCount(@ModelAttribute("year") String year) throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(df.parse(year));
		String heading = "Customer Count by Month for the year " + c.get(Calendar.YEAR);
		JSONReports report = new JSONReports();
		List<BigInteger> totals = invoiceService.getCustomerCounts(c.get(Calendar.YEAR));

		return report.JSONCreateCustomerCount(totals, heading).toString();
	}

	@RequestMapping("/reports/profitreport")
	public String getProfitReport(@ModelAttribute("year") String year) throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(df.parse(year));
		String heading = "Cost of goods sold";
		JSONReports report = new JSONReports();
		List<Double> data = ps.getCOGSReport(c.get(Calendar.YEAR));

		return report.JSONCreateCogsReport(data, heading).toString();

	}
	
	

}
