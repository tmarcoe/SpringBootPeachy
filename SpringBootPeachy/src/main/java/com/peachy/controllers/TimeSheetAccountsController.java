package com.peachy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peachy.entity.TimeSheetAccounts;
import com.peachy.service.TimeSheetAccountsService;

@Controller
public class TimeSheetAccountsController {
	private final String pageLink = "/activitypaging";
	
	@Autowired
	TimeSheetAccountsService timeSheetAccountsService;
	
	private PagedListHolder<TimeSheetAccounts> activityList;
	
	@RequestMapping("/vendor/createactivity")
	public String createActivity(Model model) {
		TimeSheetAccounts timeSheetAccounts = new TimeSheetAccounts();
		
		model.addAttribute("timeSheetAccounts", timeSheetAccounts);
		
		return "createactivity";
	}
	@RequestMapping("/vendor/activitylist")
	public String activityList(Model model) {
		
		activityList = timeSheetAccountsService.retrieveList();
		if (activityList.getSource().size() == 0) {
			
			TimeSheetAccounts timeSheetAccounts = new TimeSheetAccounts();
			
			model.addAttribute("timeSheetAccounts", timeSheetAccounts);
			
			return "createactivity";
		}
		activityList.setPage(0);
		activityList.setPageSize(15);
		model.addAttribute("objectList", activityList);
		model.addAttribute("pagelink", pageLink);
		
		return "activitylist";
	}
	
	
	@RequestMapping("/vendor/saveactivity")
	public String saveActivity(@ModelAttribute("timeSheetAccounts") TimeSheetAccounts timeSheetAccounts, BindingResult result, Model model) {
		
		timeSheetAccountsService.create(timeSheetAccounts);
		
		activityList = timeSheetAccountsService.retrieveList();
		
		if (activityList.getSource().size() == 0) {
			
			timeSheetAccounts = new TimeSheetAccounts();
			
			model.addAttribute("timeSheetAccounts", timeSheetAccounts);
			
			return "createactivity";
		}
		
		return "redirect:/vendor/activitylist";
	}
	
	@RequestMapping("/vendor/editactivity")
	public String editActivity(@ModelAttribute("accountNum") String AccountNum, BindingResult result, Model model) {
		
		TimeSheetAccounts timeSheetAccounts = timeSheetAccountsService.retrieve(AccountNum);
		
		model.addAttribute("timeSheetAccounts", timeSheetAccounts);
		
		return "editactivity";
	}
	@RequestMapping("/vendor/deleteactivity")
	public String deleteActivity(@ModelAttribute("accountNum") String AccountNum, BindingResult result, Model model) {
		TimeSheetAccounts tsa = timeSheetAccountsService.retrieve(AccountNum);
		timeSheetAccountsService.delete(tsa);
		
		activityList = timeSheetAccountsService.retrieveList();
		if (activityList.getSource().size() == 0) {
			
			TimeSheetAccounts timeSheetAccounts = new TimeSheetAccounts();
			
			model.addAttribute("timeSheetAccounts", timeSheetAccounts);
			
			return "createactivity";
		}
		
		return "redirect:/vendor/activitylist";
	}
	
	@RequestMapping("/vendor/updateactivity")
	public String updaateActivity(@ModelAttribute("timeSheetAccounts") TimeSheetAccounts timeSheeAccounts, BindingResult result, Model model) {
		
		timeSheetAccountsService.update(timeSheeAccounts);
		
		return "redirect:/vendor/activitylist";
	}

	@RequestMapping("/vendor/activitypaging")
	public String activityPaging(@ModelAttribute("page") String page, Model model) {
		
		int pgNum;

	        pgNum = isInteger(page);
	        
	        if ("next".equals(page)) {
	        	activityList.nextPage();
	        }
	        else if ("prev".equals(page)) {
	        	activityList.previousPage();
	        }else if (pgNum != -1) {
	        	activityList.setPage(pgNum);
	        }

	        
	        model.addAttribute("objectList", activityList);
			model.addAttribute("pagelink", pageLink);
	        
	        return "activitylist";
	}
	
	private int isInteger(String s) {
		int retInt;
	    try { 
	    	retInt = Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return -1; 
	    } catch(NullPointerException e) {
	        return -1;
	    }
	    // only got here if we didn't return false
	    return retInt;
	}
	

}
