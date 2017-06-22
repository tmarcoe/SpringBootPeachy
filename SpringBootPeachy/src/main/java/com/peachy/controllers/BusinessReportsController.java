package com.peachy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BusinessReportsController {
	
	@RequestMapping("/businessreports")
	public String showBusinessReports(){
		
		return "businessreports";
	}
	
	@RequestMapping("/vendor/salesreport")
	public String showSalesReport(@ModelAttribute("reportType") String reportType, @ModelAttribute("year") String year, Model model) {
		String salesData = "";
		
		switch (reportType) {
		case "sales":
			salesData = "/vendor/data-service/sales";
			break;
		case "count":
			salesData = "/vendor/data-service/customercount";
			break;
		case "profit":
			salesData = "/vendor/data-service/profitreport";
			break;
		}
		
		model.addAttribute("year", year);
		model.addAttribute("salesData", salesData);
		
		return "salesreport";
	}
	
	@RequestMapping("/vendor/gender")
	public String genderReport() {
		
		return "gender";
	}
	
	@RequestMapping("/vendor/viewsurveyreport")
	public String satisfactionSurvey() {
		
		return "viewsurveyreport";
	}
	
	@RequestMapping("/vendor/calendaryear")
	public String chooseCalendarYear(@ModelAttribute("reportType") String reportType, Model model){
		
		model.addAttribute("reportType", reportType);
		
		return "calendaryear";
	}
}
