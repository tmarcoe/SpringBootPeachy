package com.peachy.controllers;

import java.io.Serializable;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.entity.Survey;
import com.peachy.entity.UserProfile;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.service.SurveyService;
import com.peachy.service.UserProfileService;

@Controller
public class SurveyController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserProfileController.class
			.getName());
	
	PagedListHolder<Survey> objectList;
	
	private final String pageLink = "/surveypaging";
	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@RequestMapping("/user/surveyinput")
	public String surveyInput(Model model, Principal principal) {
		UserProfile user = userProfileService.retrieve(principal.getName());
		logger.info(user.getFirstname() + " " + user.getLastname() + " has filled out the survey.");
		Survey survey = new Survey();
		survey.setUser_id(user.getUser_id());
		
		model.addAttribute("survey", survey);
		
		return "surveyinput";
	}
	
	@RequestMapping("/user/submitsurvey")
	public String submitSurvey(@ModelAttribute("survey") Survey survey) {
		survey.setFilledOut(new Date());
		surveyService.create(survey);
		
		return "submitsurvey";
	}
	
	@RequestMapping("/vendor/surveylist")
	public String showSurveyList(Model model) {
		if (objectList != null) {
			objectList.getSource().clear();
			objectList = null;
			System.gc();
		}
		
		objectList = surveyService.retrieveList();
		objectList.setPage(0);
		objectList.setPageSize(10);
		
		model.addAttribute("objectList", objectList);
		model.addAttribute("pagelink", pageLink);
		 
		return "surveylist";
	}

	@RequestMapping(value = "/vendor/surveypaging", method = RequestMethod.GET)
	public String handleSurveyPaging(@ModelAttribute("page") String page, Model model, Principal principal ) throws Exception {
		
		int pgNum;

			if (objectList == null ) {
				throw new SessionTimedOutException("Your session has timed out. Please login again.");
			}

			pgNum = isInteger(page);

			if ("next".equals(page)) {
				objectList.nextPage();
			} else if ("prev".equals(page)) {
				objectList.previousPage();
			} else if (pgNum != -1) {
				objectList.setPage(pgNum);
			}

			model.addAttribute("objectList", objectList);
	        model.addAttribute("pagelink", pageLink);

			return "surveylist";
	}

	private int isInteger(String s) {
		int retInt;
		try {
			retInt = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		} catch (NullPointerException e) {
			return -1;
		}
		// only got here if we didn't return false
		return retInt;
	}
	

}
