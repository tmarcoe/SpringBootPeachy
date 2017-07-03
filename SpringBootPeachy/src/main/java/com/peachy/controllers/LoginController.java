package com.peachy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peachy.entity.UserProfile;
import com.peachy.helper.DataEncryption;
import com.peachy.service.UserProfileService;


@Controller
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserProfileService userProfileService;

	private static Logger logger = Logger.getLogger(LoginController.class
			.getName());

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping("/access-denied")
	public String showBlocked() {
		logger.error("User was blocked.");
		return "access-denied";
	}
	@RequestMapping("/login")
	public String showLogin() {
		
		return "loginJsp";
	}
	
	
	@RequestMapping("/loggedout")
	public String showLoggedOut(HttpSession session, Model model) {
		session.invalidate();

		return "loggedout";
	}

	@RequestMapping("/public/verify")
	public String verifyEmail(@ModelAttribute("puKey") String userID, @ModelAttribute("prKey") String h, Model model) throws Exception {
		String dec = DataEncryption.decode(userID);
		UserProfile user = userProfileService.retrieve(Integer.parseInt(dec));
		if (user == null) {
			logger.error("User ID " + userID + " failed to load.");
			return "error";
		}
		if (h.compareTo(user.getPassword()) == 0) {
			user.setEnabled(true);
		}else{
			logger.error("User ID " + userID + " had a wrong password.");
			return "blocked";
		}
		userProfileService.merge(user);
		String name = user.getFirstname() + " " + user.getLastname();
		model.addAttribute("name", name);

		return "verify";
	}
	
}
