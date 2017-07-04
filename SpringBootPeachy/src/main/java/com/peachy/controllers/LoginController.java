package com.peachy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
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

import com.peachy.component.FilePath;
import com.peachy.email.ProcessEmail;
import com.peachy.entity.PasswordRecovery;
import com.peachy.entity.UserProfile;
import com.peachy.helper.DataEncryption;
import com.peachy.service.PasswordRecoveryService;
import com.peachy.service.UserProfileService;

@Controller
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	PasswordRecoveryService passwordRecoveryService;

	@Autowired
	private FilePath fp;

	private static Logger logger = Logger.getLogger(LoginController.class.getName());

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
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
	public String verifyEmail(@ModelAttribute("puKey") String userID, @ModelAttribute("prKey") String h, Model model)
			throws Exception {
		String dec = DataEncryption.decode(userID);
		UserProfile user = userProfileService.retrieve(Integer.parseInt(dec));
		if (user == null) {
			logger.error("User ID " + userID + " failed to load.");
			return "error";
		}
		if (h.compareTo(user.getPassword()) == 0) {
			user.setEnabled(true);
		} else {
			logger.error("User ID " + userID + " had a wrong password.");
			return "blocked";
		}
		userProfileService.merge(user);
		String name = user.getFirstname() + " " + user.getLastname();
		model.addAttribute("name", name);

		return "verify";
	}

	@RequestMapping("/public/resetpassword")
	public String resetPassword(@ModelAttribute("puKey") String puKey, @ModelAttribute("prKey") String prKey, Model model) {
		String userId = DataEncryption.decode(puKey);
		
		PasswordRecovery pr = passwordRecoveryService.retrieve(prKey);
		if (pr == null) {
			return "nosuchuser";
		}
		UserProfile user = userProfileService.retrieve(Integer.parseInt(userId));
		if (user == null) {
			return "nosuchuser";
		}
		passwordRecoveryService.delete(pr);
		user.setPassword("");

		model.addAttribute("userProfile", user);

		return "resetpassword";
	}
	
	@RequestMapping("/public/passwordrecovery")
	public String passwordRecovery(HttpServletRequest request, @ModelAttribute("username") String username, Model model)
			throws Exception {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		UserProfile user = userProfileService.retrieve(username);

		if (user == null) {
			return "nosuchuser";
		}
		String token = DataEncryption.tokenGenerator(16);
		PasswordRecovery pass = new PasswordRecovery(token, user.getUser_id(), dt);
		passwordRecoveryService.create(pass);
		String baseUrl = String.format("%s://%s:%d/public/resetpassword?puKey=", request.getScheme(),
				request.getServerName(), request.getServerPort());
		ProcessEmail pe = new ProcessEmail(fp);
		pe.sendPasswordRecovery(user, baseUrl, token);

		return "passwordrecovery";
	}

}
